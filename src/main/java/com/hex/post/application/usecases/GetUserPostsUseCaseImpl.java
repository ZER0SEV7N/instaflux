//post/application/usecases/GetUserPostsUseCaseImpl.java
package com.hex.post.application.usecases;

import com.hex.post.domain.model.Post;
import com.hex.post.domain.ports.in.GetUserPostsUseCase;
import com.hex.post.domain.ports.out.PostRepositoryPort;

import reactor.core.publisher.Flux;

/**
 * GetUserPostsUseCaseImpl: Implementación del caso de uso para obtener los posts de un usuario específico
 * Esta clase implementa la interfaz GetUserPostsUseCase y define la lógica para obtener los posts de un usuario dado su correo electrónico.    
 */
public class GetUserPostsUseCaseImpl implements GetUserPostsUseCase {
    
    private final PostRepositoryPort postRepositoryPort;

    public GetUserPostsUseCaseImpl(PostRepositoryPort postRepositoryPort) {
        this.postRepositoryPort = postRepositoryPort;
    }

    //Metodo publico para obtener los posts de un usuario dado su correo electrónico
    public Flux<Post> getUserPosts(String authorEmail) {
        return postRepositoryPort.findByAuthorEmailOrderByCreatedAtDesc(authorEmail);
    }
}
