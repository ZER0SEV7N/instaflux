//domain/ports/in/RegisterUseCase.java
package com.hex.user.domain.ports.in;

import com.hex.user.domain.model.User;

import reactor.core.publisher.Mono;

/**
 * 
 * RegisterUseCase: Interfaz que define el caso de uso para registrar un nuevo usuario en el sistema. 
 * Contiene los métodos necesarios para llevar a cabo el proceso de registro de usuarios.
 */
public interface RegisterUseCase {
    Mono<User> register(String username, String email, String password);
}
