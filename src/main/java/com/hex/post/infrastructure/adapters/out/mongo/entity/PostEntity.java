//post/infrastructure/adapters/out/mongo/entity/PostEntity.java
package com.hex.post.infrastructure.adapters.out.mongo.entity;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entidad Post en la base de datos MongoDB.
 */
@Document(collection = "posts")
public record PostEntity(
    @Id String id,
    String title,
    String content,
    String authorId,
    Instant createdAt,
    Instant updatedAt
) {
    
}
