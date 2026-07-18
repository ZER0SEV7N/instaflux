//test/java/com/hex/follow/application/usecases/FollowUserUseCaseImplTest.java
package com.hex.follow.application.usecases;

import com.hex.follow.domain.model.Follow;
import com.hex.follow.domain.ports.out.FollowRepositoryPort;
import com.hex.user.domain.model.User;
import com.hex.user.domain.ports.out.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

/**
 * 
 * FollowUserUseCaseImplTest: Clase de prueba para FollowUserUseCaseImpl
 * Prueba unitaria para el caso de uso de seguir a un usuario
 */
class FollowUserUseCaseImplTest {
    
    //Declarar la clase que vamos a probar y sus Mocks
    private FollowRepositoryPort followRepository;
    private UserRepositoryPort userRepository;
    private FollowUserUseCaseImpl followUserUseCase;

    @BeforeEach
    void setUp() {
        //Inicializar los Mocks antes de cada prueba
        followRepository = mock(FollowRepositoryPort.class);
        userRepository = mock(UserRepositoryPort.class);

        //Inicializar la clase que vamos a probar con los Mocks
        followUserUseCase = new FollowUserUseCaseImpl(followRepository, userRepository);
    }

    @Test
    @DisplayName("Debe lanzar error si el usuario intenta seguirse a si mismo")
    void follow_Fails_WhenUserFollowsSelf() {
        //Arrange & Act
        Mono<Void> result = followUserUseCase.follow("mi_email@test.com", "mi_email@test.com");
        //Assert
        StepVerifier.create(result)
                .expectErrorMessage("No puedes seguirte a ti mismo")
                .verify();
    }

    @Test
    @DisplayName("Debe lanzar error si el usuario a seguir no existe")
    void follow_Fails_WhenUserToFollowDoesNotExist() {
        //Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Mono.empty());
        //Act
        Mono<Void> result = followUserUseCase.follow("mi_email@test.com", "otro_email@test.com");
        //Assert
        StepVerifier.create(result)
                .expectErrorMessage("El usuario a seguir no existe")
                .verify();
    }

    @Test
    @DisplayName("Debe lanzar error si el usuario ya sigue al otro usuario")
    void follow_Fails_WhenAlreadyFollowing() {
        //Arrange
        User mockUser = new User("1", "Destino", "destino@test.com", "password", "bio" );
        when(userRepository.findByEmail("destino@test.com")).thenReturn(Mono.just(mockUser));

        //Simular que ya existe un seguimiento entre los usuarios
        when(followRepository.existsByFollowerAndFollowing("mi_email@test.com", "destino@test.com"))
            .thenReturn(Mono.just(true));

        //Act
        Mono<Void> result = followUserUseCase.follow("mi_email@test.com", "destino@test.com");
        //Assert
        StepVerifier.create(result)
                .expectErrorMessage("Ya sigues a este usuario")
                .verify();
    }

    @Test
    @DisplayName("Debe seguir a un usuario correctamente")
    void follow_Success() {
        //Arrange
        User mockUser = new User("1", "Destino", "destino@test.com", "password", "bio" );
        when(userRepository.findByEmail("destino@test.com")).thenReturn(Mono.just(mockUser));

        //Simular que no existe un seguimiento entre los usuarios
        when(followRepository.existsByFollowerAndFollowing("mi_email@test.com", "destino@test.com"))
            .thenReturn(Mono.just(false));

        //Simular el guardado exitoso del seguimiento
        Follow mockFollow = new Follow("100", "mi_email@test.com", "destino@test.com", Instant.now());
        when(followRepository.save(any(Follow.class))).thenReturn(Mono.just(mockFollow));

        //Act
        Mono<Void> result = followUserUseCase.follow("mi_email@test.com", "destino@test.com");
 
        //Assert: Verificar que el resultado sea exitoso y complete sin errores
        StepVerifier.create(result)
                .verifyComplete();
    }
}
