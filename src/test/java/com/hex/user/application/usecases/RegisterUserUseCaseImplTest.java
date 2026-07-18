//test/java/com/hex/user/application/usecases/RegisterUserUseCaseImplTest.java
package com.hex.user.application.usecases;

import com.hex.user.domain.model.User;
import com.hex.user.domain.ports.out.PasswordEncoderPort;
import com.hex.user.domain.ports.out.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * 
 * RegisterUserUseCaseImplTest: Clase de prueba para RegisterUserUseCaseImpl
 * Prueba unitaria para el caso de uso de registro de usuario
 */
class RegisterUserUseCaseImplTest {

    //Declarar la clase que vamos a probar y sus Mocks
    private UserRepositoryPort userRepository;
    private PasswordEncoderPort passwordEncoder;
    private RegisterUseCaseImpl registerUseCase;

    //Inicializar los Mocks y la clase que vamos a probar antes de cada prueba
    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepositoryPort.class);
        passwordEncoder = Mockito.mock(PasswordEncoderPort.class);
        registerUseCase = new RegisterUseCaseImpl(userRepository, passwordEncoder);
    }

    @Test
    @DisplayName("Debe registrar usuario exitosamente si el email no existe")
    void register_Success() {
        //Arrange: El email NO existe
        when(userRepository.findByEmail("nuevo@test.com")).thenReturn(Mono.empty());
        //Simulamos la encriptación
        when(passwordEncoder.encode("12345")).thenReturn("hashed_12345");
        
        //Simulamos que Mongo nos devuelve el usuario ya con su ID generado
        User savedUser = new User("1", "nuevo_user", "nuevo@test.com", "hashed_12345", null);
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(savedUser));

        //Act
        Mono<User> result = registerUseCase.register("nuevo_user", "nuevo@test.com", "12345");

        //Assert
        StepVerifier.create(result)
                .expectNextMatches(user -> 
                        user.id().equals("1") && 
                        user.password().equals("hashed_12345") && //Verificamos que guardó la contraseña encriptada
                        user.email().equals("nuevo@test.com"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe lanzar error si el email ya está registrado")
    void register_Fails_WhenEmailAlreadyExists() {
        //Arrange: Simular que la BD encuentra a un usuario con ese correo
        User existingUser = new User("1", "viejo_user", "existe@test.com", "hash", "bio");
        when(userRepository.findByEmail("existe@test.com")).thenReturn(Mono.just(existingUser));

        //Act
        Mono<User> result = registerUseCase.register("intruso", "existe@test.com", "12345");

        //Assert
        StepVerifier.create(result)
                .expectErrorMessage("El Email ya está registrado")
                .verify();
    }
}