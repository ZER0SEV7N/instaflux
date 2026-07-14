//application/usecases/LoginUserUseCaseImpl.java
package com.hex.instaflux.user.application.usecases;

import com.hex.instaflux.user.domain.ports.out.JwtPort;
import com.hex.instaflux.user.domain.ports.out.PasswordEncoderPort;
import com.hex.instaflux.user.domain.ports.out.UserRepositoryPort;

import reactor.core.publisher.Mono;

/**
 * 
 * LoginUserUseCaseImpl: Implementación del caso de uso para iniciar sesión de un usuario en el sistema.
 * Esta clase se encarga de coordinar la lógica de negocio necesaria para autenticar a un usuario, 
 * incluyendo la verificación de credenciales y la generación de un token JWT.
 * 
 */
public class LoginUserUseCaseImpl implements LoginUseCase {

    private final UserRepositoryPort userRepository; //Interfaz para interactuar con la capa de persistencia de datos
    private final PasswordEncoderPort passwordEncoder; //Interfaz para la codificación de contrase
    private final JwtPort jwtPort; //Interfaz para la generación y validación de tokens JWT

    public LoginUserUseCaseImpl(UserRepositoryPort userRepository, PasswordEncoderPort passwordEncoder, JwtPort jwtPort) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtPort = jwtPort;
    }

    @Override
    public Mono<String> login(String email, String password) {
        return userRepository.findByEmail(email)
            //Si el usuario con el correo electrónico proporcionado no existe, lanza una excepción
            .switchIfEmpty(Mono.error(new RuntimeException("Credenciales invalidas")))
            .flatMap(user -> {
                //Verifica si la contraseña proporcionada coincide con la contraseña almacenada en la base de datos
                if (passwordEncoder.matches(password, user.password()))
                    //Si las credenciales son válidas, genera un token JWT para el usuario
                    return Mono.just(jwtPort.generateToken(user.id()));
                else
                    //Si las credenciales son inválidas, lanza una excepción
                    return Mono.error(new RuntimeException("Credenciales invalidas"));
            });
        
    }
}
