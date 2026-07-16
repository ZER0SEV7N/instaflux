//post/domain/ports/out/PostRepositoryPort.java
package com.hex.post.domain.ports.out;

import com.hex.post.domain.model.Post;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interfaz para el repositorio de posts
 */
public interface PostRepositoryPort {
    //Ordernar los posts por fecha de creación en orden descendente y devolver una lista de posts
    Flux<Post> findAllOrderByCreatedAtDesc(); 
    
    Mono<Post> save(Post post); //Guardar un post en el repositorio y devolver el post guardado

    Mono<Post> findById(String id); //Buscar un post por su id y devolver el post encontrado
}
