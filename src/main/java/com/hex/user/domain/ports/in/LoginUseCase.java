//domain/ports/in/LoginUseCase.java
package com.hex.user.domain.ports.in;

import reactor.core.publisher.Mono;

/**
 * 
 * LoginUseCase: Interfaz que define el caso de uso para iniciar sesión en el sistema. 
 * Contiene el método necesario para llevar a cabo el proceso de autenticación de usuarios.
 */
public interface LoginUseCase {
     //Devuelve un Mono con el Token JWT en caso de éxito
    Mono<String> login(String email, String password);
}
