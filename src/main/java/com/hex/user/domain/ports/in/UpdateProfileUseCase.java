//user/domain/ports/in/UpdateProfileUseCase.java
package com.hex.user.domain.ports.in;

import com.hex.user.domain.model.User;
import reactor.core.publisher.Mono;

/**
 * Interfaz para el caso de uso de actualización de perfil de usuario
 * UpdateProfileUseCase
 */
public interface UpdateProfileUseCase {
    Mono<User> updateBio(String email, String newBio); //Metodo para actualizar la biografía de un usuario dado su correo electrónico
    Mono<User> getUserByEmail(String email); //Metodo para obtener un usuario dado su correo electrónico
}
