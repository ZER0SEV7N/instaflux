//post/domain/ports/in/LikePostUseCase.java
package com.hex.post.domain.ports.in;

import com.hex.post.domain.model.Post;
import reactor.core.publisher.Mono;

/**
 * LikePostUseCase: Interfaz de entrada para el caso de uso de dar like a un post/publicacion
 */
public interface LikePostUseCase {
    Mono<Post> likePost(String postId); //Incrementar el contador de likes de un post y devolver el post actualizado
}
