//user/domain/ports/in/UpdateProfileUseCase.java
package com.hex.user.application.usecases;

import com.hex.user.domain.model.User;
import com.hex.user.domain.ports.in.UpdateProfileUseCase;
import com.hex.user.domain.ports.out.UserRepositoryPort;

import reactor.core.publisher.Mono;

/**
 * 
 * UpdateProfileUseCaseImpl: Implementacion del caso de uso para actualizar el perfil de un usuario
 */
public class UpdateProfileUseCaseImpl implements UpdateProfileUseCase {
    
    private final UserRepositoryPort userRepository;

    public UpdateProfileUseCaseImpl(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    //Metodo publico para actualizar la biografía de un usuario dado su correo electrónico
    public Mono<User> updateBio(String email, String newBio) {
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado"))) //Si no se encuentra el usuario, devuelve un error
                .map(user -> user.updateBio(newBio)) //Actualiza la biografía del usuario
                .flatMap(userRepository::save); //Guarda el usuario actualizado en el repositorio
    }

    //Metodo publico para obtener un usuario dado su correo electrónico
    public Mono<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado"))); //Si no se encuentra el usuario, devuelve un error
    }
    
}
