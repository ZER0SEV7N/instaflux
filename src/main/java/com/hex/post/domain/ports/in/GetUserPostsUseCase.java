//post/domain/ports/in/GetUserPostsUseCase.java
package com.hex.post.domain.ports.in;

import com.hex.post.domain.model.Post;

import reactor.core.publisher.Flux;

/**
 * 
 * GetUserPostsUseCase: Interfaz para el caso de uso de obtención de posts de un usuario específico
 * Esta interfaz define el contrato para obtener los posts de un usuario dado su correo electrónico.
 */
public interface GetUserPostsUseCase {
    Flux<Post> getUserPosts(String authorEmail); //Metodo para obtener los posts de un usuario dado su correo electrónico
}
