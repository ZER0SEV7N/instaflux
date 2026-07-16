//post/infrastructure/adapters/in/dto/PostResponse.java
package com.hex.post.infrastructure.adapters.in.dto;

import java.time.Instant;

//DTO para la respuesta de un post
public record PostResponse(
    String id,
    String authorEmail,
    String content, 
    String imageUrl,
    int likes,
    Instant createdAt
) {
    
}
