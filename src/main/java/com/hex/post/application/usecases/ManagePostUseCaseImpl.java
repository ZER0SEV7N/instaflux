//post/application/usecases/ManagePostUseCaseImpl.java
package com.hex.post.application.usecases;

import com.hex.post.domain.model.Post;
import com.hex.post.domain.ports.in.ManagePostUseCase;
import com.hex.post.domain.ports.out.PostRepositoryPort;

import reactor.core.publisher.Mono;

/**
 * 
 * ManagePostUseCaseImpl: Implementacion del caso de uso para la gestión de publicaciones
 */
public class ManagePostUseCaseImpl implements ManagePostUseCase{
   
    private final PostRepositoryPort postRepository;

    public ManagePostUseCaseImpl(PostRepositoryPort postRepository) {
        this.postRepository = postRepository;
    }

    //Metodo para editar un post, validando que el usuario que intenta editar sea el dueño del post
    public Mono<Post> editPost(String postId, String userEmail, String newContent, String imageUrl) {
        return postRepository.findById(postId)
                .switchIfEmpty(Mono.error(new RuntimeException("Post no encontrado")))
                    .flatMap(post -> {
                        //Validar que el usuario que intenta editar sea el dueño real del post
                        if(!post.authorEmail().equals(userEmail)) return Mono.error(new RuntimeException("No tienes permiso para editar este post"));
                        
                        //Crear el nuevo registro inmutable
                        Post updatedPost = new Post(post.id(), post.authorEmail(), newContent, imageUrl, post.likes(), post.createdAt());
                        return postRepository.save(updatedPost);
                    });
    }

    //Metodo para eliminar un post, validando que el usuario que intenta eliminar sea el dueño del post
    public Mono<Void> deletePost(String postId, String userEmail) {
        return postRepository.findById(postId)
                .switchIfEmpty(Mono.error(new RuntimeException("Post no encontrado")))
                    .flatMap(post -> {
                        //Validar que el usuario que intenta eliminar sea el dueño real del post
                        if(!post.authorEmail().equals(userEmail)) return Mono.error(new RuntimeException("No tienes permiso para eliminar este post"));
                        
                        return postRepository.deleteById(postId);
                    });
    }
}
