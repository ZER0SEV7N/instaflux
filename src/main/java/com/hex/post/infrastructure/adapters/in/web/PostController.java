//src/main/java/com/hex/post/infrastructure/adapters/in/web/PostController.java
package com.hex.post.infrastructure.adapters.in.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hex.global.infrastructure.web.ResponseGlobal;
import com.hex.post.domain.model.Post;
import com.hex.post.domain.ports.in.CreatePostUseCase;
import com.hex.post.domain.ports.in.GetFeedUseCase;
import com.hex.post.domain.ports.in.GetUserPostsUseCase;
import com.hex.post.domain.ports.in.LikePostUseCase;
import com.hex.post.domain.ports.out.ImageStoragePort;
import com.hex.post.infrastructure.adapters.in.dto.PostResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 
 * PostController: Controlador REST para manejar las solicitudes relacionadas con la entidad Post.
 * Esta clase define los endpoints de la API para realizar operaciones CRUD sobre los posts.
 */
@RestController
@RequestMapping("/api/posts")
public class PostController {
    
    private final CreatePostUseCase createPostUseCase;
    private final GetFeedUseCase getFeedUseCase;
    private final LikePostUseCase likePostUseCase;
    private final GetUserPostsUseCase getUserPostsUseCase;
    private final ImageStoragePort imageStoragePort;

    public PostController(CreatePostUseCase createPostUseCase, GetFeedUseCase getFeedUseCase, LikePostUseCase likePostUseCase, GetUserPostsUseCase getUserPostsUseCase, ImageStoragePort imageStoragePort) {
        this.createPostUseCase = createPostUseCase;
        this.getFeedUseCase = getFeedUseCase;
        this.likePostUseCase = likePostUseCase;
        this.getUserPostsUseCase = getUserPostsUseCase;
        this.imageStoragePort = imageStoragePort;
    }

    /**
     * Endpoint para crear un nuevo post. 
     * Este método recibe una solicitud de creación de post, 
     * obtiene el email del usuario autenticado desde el contexto de seguridad reactivo, y llama al caso de uso para crear el post.
     * POST: /api/posts
     * @param request: {content: "Contenido del post", imageUrl: "URL de la imagen"}
     * @return: ResponseGlobal<PostResponse>: Respuesta global con el estado de la operación y los datos del post creado.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseGlobal<PostResponse>> createPost(
            @RequestPart("content") String content,
            @RequestPart("file") FilePart file) {
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> context.getAuthentication().getName()) //Obtener el email del usuario autenticado desde el contexto de seguridad reactivo
                //Primero subimos la imagen al almacenamiento y obtenemos la URL de la imagen
                .flatMap(authorEmail -> 
                    imageStoragePort.uploadImage(file)
                        //Cuando la imagen se guarde y nos devuelva la URL, creamos el post en Mongo
                        .flatMap(imageUrl -> createPostUseCase.createPost(authorEmail, content, imageUrl))
                )
                .map(this::toResponse)
                .map(response -> ResponseGlobal.success(HttpStatus.CREATED.value(), response, "Post creado exitosamente"));
    }

    /**
     * Endpoint para obtener el feed de posts.
     * Este método llama al caso de uso para obtener todos los posts ordenados por fecha de creación descendente y los devuelve en un Flux dentro de una respuesta global.
     * GET: /api/posts
     * @return ResponseGlobal<Flux<PostResponse>>: Respuesta global con el estado de la operación y los datos del feed de posts.
     */
    @GetMapping
    public Mono<ResponseGlobal<List<PostResponse>>> getFeed() {
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> context.getAuthentication().getName()) // Sacamos el email del token
                .flatMap(userEmail -> getFeedUseCase.getFeed(userEmail)
                        .map(this::toResponse)
                        .collectList() // Agrupamos el flujo reactivo en una lista para el JSON
                )
                .map(posts -> ResponseGlobal.success(200, posts, "Feed personalizado obtenido exitosamente"));
    }

    /**
     * Endpoint para dar like a un post.
     * Este método recibe el ID del post, obtiene el email del usuario autenticado desde el contexto de seguridad reactivo, y llama al caso de uso para dar like al post.
     * POST: /api/posts/{postId}/like
     * @param postId: ID del post al que se le dará like.
     * @return ResponseGlobal<PostResponse>: Respuesta global con el estado de la operación y los datos del post actualizado.
     */
    @PostMapping("/{postId}/like")
    public Mono<ResponseGlobal<PostResponse>> likePost(@PathVariable String postId) {
        return likePostUseCase.likePost(postId)
                .map(this::toResponse)
                .map(response -> ResponseGlobal.success(HttpStatus.OK.value(), response, "Like agregado exitosamente"));
    }

    /**
     * Endpoint para obtener todos los posts de un usuario específico.
     * Este método recibe el correo electrónico del autor y llama al caso de uso para obtener todos
     * los posts de ese autor ordenados por fecha de creación descendente.
     * GET: /api/posts/user/{authorEmail}
     * @param authorEmail: Correo electrónico del autor cuyos posts se desean obtener.
     * @return ResponseGlobal<Flux<PostResponse>>: Respuesta global con el estado de la operación y los datos de los posts del usuario.
     * @throws RuntimeException: Si no se encuentran posts para el autor especificado.
     */
    @GetMapping("/user/{authorEmail}")
    public Mono<ResponseGlobal<Flux<PostResponse>>> getUserPosts(@PathVariable String authorEmail) {
        Flux<PostResponse> userPosts = getUserPostsUseCase.getUserPosts(authorEmail).map(this::toResponse);
        return Mono.just(ResponseGlobal.success(HttpStatus.OK.value(), userPosts, "Posts del usuario obtenidos exitosamente"));
    }

    //Mapper
    //Método privado para convertir un objeto Post a PostResponse
    private PostResponse toResponse(Post post) {
        return new PostResponse(post.id(), post.authorEmail(), post.content(), post.imageUrl(), post.likes(), post.createdAt());
    }
}
