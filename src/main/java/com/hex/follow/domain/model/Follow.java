//follow/domain/model/Follow.java
package com.hex.follow.domain.model;

import java.time.Instant;

//Modelo para la entidad Follow
public record Follow(
    String id,
    String followerEmail, //El que cliquea seguir
    String followingEmail, //A quien se le da follow
    Instant createdAt
) {
    
}
