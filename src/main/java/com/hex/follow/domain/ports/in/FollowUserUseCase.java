//follow/domain/ports/in/FollowUserUseCase.java
package com.hex.follow.domain.ports.in;

import reactor.core.publisher.Mono;

//Interfaz del puerto de entrada para la entidad Follow
public interface FollowUserUseCase {
    Mono<Void> follow(String followerEmail, String followingEmail);
    Mono<Void> unfollow(String followerEmail, String followingEmail);
}
