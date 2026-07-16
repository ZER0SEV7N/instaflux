//post/application/usecases/LikePostUseCaseImpl.java
package com.hex.post.application.usecases;

import com.hex.post.domain.model.Post;
import com.hex.post.domain.ports.in.LikePostUseCase;
import com.hex.post.domain.ports.out.PostRepositoryPort;

import reactor.core.publisher.Mono;

/**
 * 
 * LikePostUseCaseImpl: Implementacion del caso de uso para dar like a un post/publicacion
 */
public class LikePostUseCaseImpl implements LikePostUseCase {
    
    private final PostRepositoryPort postRepository;

    public LikePostUseCaseImpl(PostRepositoryPort postRepository) {
        this.postRepository = postRepository;
    }

    //Metodo publico para dar like a un Post
    public Mono<Post> likePost(String postId){
        return postRepository.findById(postId)
                .switchIfEmpty(Mono.error(new RuntimeException("Post no encontrado"))) //Si no se encuentra el post, devuelve un error
                .map(Post::addLike) //Incrementa el contador de likes del post
                .flatMap(postRepository::save); //Guarda el post actualizado en el repositorio
    }
}
