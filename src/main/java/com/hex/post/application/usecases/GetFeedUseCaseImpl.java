//post/application/usecases/GetFeedUseCaseImpl.java
package com.hex.post.application.usecases;

import com.hex.follow.domain.ports.out.FollowRepositoryPort;
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
    private final FollowRepositoryPort followRepository;

    public GetFeedUseCaseImpl (PostRepositoryPort postRepositoryPort, FollowRepositoryPort followRepository) {
        this.postRepositoryPort = postRepositoryPort;
        this.followRepository = followRepository;
    }
    
    //Metodo publico para obtener el feed de publicaciones
    public Flux<Post> getFeed(String userEmail) {
        //Buscar a quien sigue al usuario
        return followRepository.findByFollowerEmail(userEmail)
                //Extaer solo el correo de los usuarios que sigue
                .map(follow -> follow.followingEmail())
                //Agrupar los correos en una lista
                .collectList()
                //Buscar los posts de los usuarios que sigue y del usuario mismo, ordenados por fecha de creación descendente
                .flatMapMany(followingEmails -> {
                    followingEmails.add(userEmail); 
                    return postRepositoryPort.findByAuthorEmailInOrderByCreatedAtDesc(followingEmails);
                });
    }
}
