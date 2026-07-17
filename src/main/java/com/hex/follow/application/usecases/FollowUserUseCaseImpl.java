//follow/application/usecases/FollowUserUseCaseImpl.java
package com.hex.follow.application.usecases;

import java.time.Instant;

import com.hex.follow.domain.model.Follow;
import com.hex.follow.domain.ports.in.FollowUserUseCase;
import com.hex.follow.domain.ports.out.FollowRepositoryPort;
import com.hex.user.domain.ports.out.UserRepositoryPort;

import reactor.core.publisher.Mono;

/**
 * 
 * FollowUserUseCaseImpl: Implementación de la interfaz FollowUserUseCase, que define los casos de uso para seguir y dejar de seguir a un usuario.
 * Contiene la lógica de negocio para manejar las operaciones de seguimiento entre usuarios.
 */
public class FollowUserUseCaseImpl implements FollowUserUseCase {
    
    private final FollowRepositoryPort followRepository;
    private final UserRepositoryPort userRepository;

    public FollowUserUseCaseImpl(FollowRepositoryPort followRepository, UserRepositoryPort userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    //Metodo para seguir a un usuario, verificando primero que ambos usuarios existan y que no exista ya un seguimiento entre ellos.
    public Mono<Void> follow(String followerEmail, String targetEmail) {
        //Validar que no se siga a uno mismo
        if (followerEmail.equals(targetEmail)) return Mono.error(new IllegalArgumentException("No puedes seguirte a ti mismo"));
        

        //Buscar si ambos usuarios existen y si ya existe un seguimiento entre ellos
        return userRepository.findByEmail(targetEmail)
            .switchIfEmpty(Mono.error(new RuntimeException("El usuario a seguir no existe")))
            //Comprobar si ya lo sigue
            .flatMap(user -> followRepository.existsByFollowerAndFollowing(followerEmail, targetEmail))
            .flatMap(alredyFollowing -> {
                if(alredyFollowing) return Mono.error(new RuntimeException("Ya sigues a este usuario"));
                //Si no existe un seguimiento, crear uno nuevo
                Follow newFollow = new Follow(null, followerEmail, targetEmail, Instant.now());
                return followRepository.save(newFollow);
            })
            .then();
    }

    //Metodo para dejar de seguir a un usuario, verificando primero que ambos usuarios existan y que exista un seguimiento entre ellos.
    public Mono<Void> unfollow(String followerEmail, String targetEmail) {
        return followRepository.deleteByFollowerAndFollowing(followerEmail, targetEmail);
    }
        
}
