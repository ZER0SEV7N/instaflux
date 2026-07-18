//post/domain/ports/in/ManagePostUseCase.java
package com.hex.post.domain.ports.in;

import com.hex.post.domain.model.Post;
import reactor.core.publisher.Mono;

/**
 * 
 * ManagePostUseCase: Interfaz de entrada para el caso de uso de gestión de publicaciones
 */
public interface ManagePostUseCase {
   Mono<Void> deletePost(String postId, String userEmail); //Eliminar un post por su id 
   Mono<Post> editPost(String postId, String authorEmail, String newContent, String imageUrl); //Editar un post existente con los nuevos datos proporcionados
}
