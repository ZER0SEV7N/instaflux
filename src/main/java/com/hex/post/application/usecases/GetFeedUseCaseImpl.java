//post/application/usecases/GetFeedUseCaseImpl.java
package com.hex.post.application.usecases;

import com.hex.post.domain.model.Post;
import com.hex.post.domain.ports.in.GetFeedUseCase;
import com.hex.post.domain.ports.out.PostRepositoryPort;
import reactor.core.publisher.Flux;

/**
 * 
 * GetFeedUseCaseImpl: Implementacion del caso de uso para obtener el feed de publicaciones
 */
public class GetFeedUseCaseImpl implements GetFeedUseCase {

    private final PostRepositoryPort postRepositoryPort;

    public GetFeedUseCaseImpl (PostRepositoryPort postRepositoryPort) {
        this.postRepositoryPort = postRepositoryPort;
    }
    
    //Metodo publico para obtener el feed de publicaciones
    public Flux<Post> getFeed() {
        return postRepositoryPort.findAllOrderByCreatedAtDesc();
    }
}
