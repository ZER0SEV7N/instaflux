//post/domain/ports/in/GetFeedUseCase.java
package com.hex.post.domain.ports.in;

import com.hex.post.domain.model.Post;
import reactor.core.publisher.Flux;

/**
 * 
 * GetFeedUseCase: Interfaz de entrada para el caso de uso de obtener el feed de publicaciones
 */
public interface GetFeedUseCase {
    Flux<Post> getFeed(); //Obtener el feed de publicaciones y devolver una lista de posts    
}
