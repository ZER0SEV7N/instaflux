//follow/infrastructure/adapters/out/mongo/entity/FollowEntity.java
package com.hex.follow.infrastructure.adapters.out.mongo.entity;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
 * FollowEntity: Clase que representa la entidad Follow en la base de datos MongoDB. 
 * Esta clase se utiliza para mapear los datos de seguimiento entre usuarios en la base de datos.
 * Contiene los atributos necesarios para almacenar la información de seguimiento, 
 * como el correo electrónico del seguidor, el correo electrónico del seguido y la fecha de creación del seguimiento.
 */
@Document(collection = "follows")
public record FollowEntity(
    @Id String id,
    String followerEmail, //El que cliquea seguir
    String followingEmail, //A quien se le da follow
    Instant createdAt
) { }
