//infrastructure/adapters/out/mongo/entity/UserEntity.java
package com.hex.user.infrastructure.adapters.out.mongo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
 * UserEntity: Clase de entidad que representa la estructura de datos del usuario en la base de datos MongoDB. 
 * Esta clase se utiliza para mapear los datos del usuario entre la base de datos y el dominio de la aplicación.
 */
@Document(collection = "users")
public record UserEntity(
    @Id String id,
    String username,
    String email,
    String password
) {
    
}
