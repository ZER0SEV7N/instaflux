//application/usecases/RegisterUserUseCase.java
package com.hex.instaflux.user.application.usecases;

import com.hex.instaflux.user.domain.model.User;
import com.hex.instaflux.user.domain.ports.out.PasswordEncoderPort;
import com.hex.instaflux.user.domain.ports.out.UserRepositoryPort;

import reactor.core.publisher.Mono;

/**
 * 
 * RegisterUserUseCase: Caso de uso para registrar un nuevo usuario en el sistema. 
 * Esta clase se encarga de coordinar la lógica de negocio necesaria para crear un nuevo usuario, 
 * incluyendo la validación de datos, la codificación de la contraseña y la generación de un token JWT.
 */
public class RegisterUserUseCaseImpl implements RegisterUseCase {

    private final UserRepositoryPort userRepository; //Interfaz para interactuar con la capa de persistencia de datos
    private final PasswordEncoderPort passwordEncoder; //Interfaz para la codificación de contraseñas
    
    public RegisterUserUseCaseImpl(UserRepositoryPort userRepository, PasswordEncoderPort passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //Sobrescribe el método register de la interfaz RegisterUseCase para implementar la lógica de registro de un nuevo usuario.
    @Override
    public Mono<User> register(String username, String email, String password) {
        //Verifica si el correo electrónico ya está registrado en el sistema
        return userRepository.findByEmail(email)
            //Si el correo electrónico ya existe, lanza una excepción
            .flatMap(existingUser -> Mono.<User>error(new RuntimeException("El Email ya está registrado")));
            //Si el Mono esta vacio (no existe un usuario con ese correo), se procede a crear un nuevo usuario
            .switchIfEmpty(Mono.defer(()->{
                String hashedPassword = passwordEncoder.encode(password);
                User newUser = new User(null, username, email, hashedPassword);
                return userRepository.save(newUser);
            }));

    }
}
