//follow/domain/ports/out/FollowRepositoryPort.java
package com.hex.follow.domain.ports.out;

import com.hex.follow.domain.model.Follow;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//Interfaz del puerto de salida para la entidad Follow
public interface FollowRepositoryPort {
    
    //Metodo para guardar un follow en la base de datos
    Mono<Follow> save(Follow follow);
    //Metodo para eliminar un follow de la base de datos
    Mono<Void> deleteByFollowerAndFollowing(String followerEmail, String followingEmail);
    //Metodo para buscar si existe un follow entre dos usuarios
    Mono<Boolean> existsByFollowerAndFollowing(String followerEmail, String followingEmail);

    //Metodo para obtener listas de seguidores y seguidos
    Flux<Follow> findByFollowerEmail(String followerEmail);
    Flux<Follow> findByFollowingEmail(String followingEmail);
}
