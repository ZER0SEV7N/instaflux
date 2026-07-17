//post/domain/ports/in/GetFeedUseCase.java
package com.hex.post.domain.ports.in;

import com.hex.post.domain.model.Post;
import reactor.core.publisher.Flux;

/**
 * 
 * GetFeedUseCase: Interfaz de entrada para el caso de uso de obtener el feed de publicaciones
 */
public interface GetFeedUseCase {
    Flux<Post> getFeed(String userEmail); //Obtener el feed de publicaciones para un usuario específico, 
    //basado en los usuarios que sigue y ordenado por fecha de creación en orden descendente
}
