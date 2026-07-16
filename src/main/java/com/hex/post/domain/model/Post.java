//post/domain/model/Post.java
package com.hex.post.domain.model;

import java.time.Instant;

/**
 * Clase de modelo de Post (Record)
 * 
 */
public record Post(
    String id,
    String authorEmail,
    String Content,
    String imageUrl,
    int likes,
    Instant createdAt
) {

    //Constructor para incrementar el contador de likes del post y devolver una nueva instancia de Post con el contador actualizado.
    public Post addLike() {
        return new Post(id, authorEmail, Content, imageUrl, likes + 1, createdAt);
    }
}
