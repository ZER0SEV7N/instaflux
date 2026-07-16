//post/domain/ports/in/CreatePostUseCase.java
package com.hex.post.domain.ports.in;

import com.hex.post.domain.model.Post;

import reactor.core.publisher.Mono;

/**
 * CreatePostUseCase: Interfaz de entrada para el caso de uso de crear un post/publicacion
 */
public interface CreatePostUseCase {
    Mono<Post> createPost(String authorEmail, String content, String imageUrl);
}
