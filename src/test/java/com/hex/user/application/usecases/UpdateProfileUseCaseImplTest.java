//test/java/com/hex/user/application/usecases/UpdateProfileUseCaseImplTest.java
package com.hex.user.application.usecases;

import com.hex.user.domain.model.User;
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
 * UpdateProfileUseCaseImplTest: Clase de prueba para UpdateProfileUseCaseImpl
 * Prueba unitaria para el caso de uso de actualizar perfil de usuario
 */
class UpdateProfileUseCaseImplTest {

    //Declarar la clase que vamos a probar y sus Mocks
    private UserRepositoryPort userRepository;
    private UpdateProfileUseCaseImpl updateProfileUseCase;

    //Inicializar los Mocks y la clase que vamos a probar antes de cada prueba
    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepositoryPort.class);
        updateProfileUseCase = new UpdateProfileUseCaseImpl(userRepository);
    }

    @Test
    @DisplayName("Debe actualizar la biografía de un usuario existente")
    void updateBio_Success() {
        //Arrange
        String email = "hex@test.com";
        String newBio = "Nueva biografía genial";
        
        //Simular que el usuario ya existe
        User existingUser = new User("1", "hexuser", email, "pass", "Vieja biografía");
        when(userRepository.findByEmail(email)).thenReturn(Mono.just(existingUser));
        
        //Simular que el guardado devuelve el usuario con la bio ya cambiada
        User updatedUser = new User("1", "hexuser", email, "pass", newBio);
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(updatedUser));

        //Act
        Mono<User> result = updateProfileUseCase.updateBio(email, newBio);

        //Assert
        StepVerifier.create(result)
                .expectNextMatches(user -> user.bio().equals(newBio))
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe lanzar error al intentar actualizar si el usuario no existe")
    void updateBio_Fails_WhenUserDoesNotExist() {
        //Arrange
        String email = "fantasma@test.com";
        when(userRepository.findByEmail(email)).thenReturn(Mono.empty());

        //Act
        Mono<User> result = updateProfileUseCase.updateBio(email, "Mi nueva bio");

        //Assert
        StepVerifier.create(result)
                .expectErrorMessage("Usuario no encontrado")
                .verify();
    }
}