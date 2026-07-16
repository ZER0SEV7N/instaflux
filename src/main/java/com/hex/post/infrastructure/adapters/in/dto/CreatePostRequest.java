//post/infrastructure/adapters/in/dto/CreatePostRequest.java
package com.hex.post.infrastructure.adapters.in.dto;

//DTO para la solicitud de creación de un post
public record CreatePostRequest(
    String content,
    String imageUrl
) { }
