//post/application/usecases/CreatePostUseCaseImpl.java
package com.hex.post.application.usecases;

import java.time.Instant;
import com.hex.post.domain.model.Post;
import com.hex.post.domain.ports.in.CreatePostUseCase;
import com.hex.post.domain.ports.out.PostRepositoryPort;
import reactor.core.publisher.Mono;

/**
 * 
 * CreatePostUseCaseImpl: Implementacion del caso de uso para la creacion de un post/publicacion
 */
public class CreatePostUseCaseImpl implements CreatePostUseCase {

    private final PostRepositoryPort postRepositoryPort;

    public CreatePostUseCaseImpl(PostRepositoryPort postRepositoryPort) {
        this.postRepositoryPort = postRepositoryPort;
    }

    //Metodo publico para crear un Post
    public Mono<Post> createPost(String authorEmail, String content, String imageUrl) {
        Post newPost =  new Post(null, authorEmail, content, imageUrl, 0, Instant.now());
        return postRepositoryPort.save(newPost);
    }

}
