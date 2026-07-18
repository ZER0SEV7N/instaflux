//test/java/com/hex/user/application/usecases/LoginUserUseCaseImplTest.java
package com.hex.user.application.usecases;

import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.hex.user.domain.model.User;
import com.hex.user.domain.ports.out.JwtPort;
import com.hex.user.domain.ports.out.PasswordEncoderPort;
import com.hex.user.domain.ports.out.UserRepositoryPort;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * 
 * LoginUserUseCaseImplTest: Clase de prueba para LoginUserUseCaseImpl
 * Prueba unitaria para el caso de uso de login de usuario
 */
class LoginUserUseCaseImplTest {
    
    //Declarar las dependencias del caso de uso y sus Mocks
    private UserRepositoryPort userRepository;
    private PasswordEncoderPort passwordEncoder;
    private JwtPort jwtPort;
    private LoginUseCaseImpl loginUseCase;

    //Inicializar los Mocks y la clase que vamos a probar antes de cada prueba
    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepositoryPort.class);
        passwordEncoder = Mockito.mock(PasswordEncoderPort.class);
        jwtPort = Mockito.mock(JwtPort.class);
        
        loginUseCase = new LoginUseCaseImpl(userRepository, passwordEncoder, jwtPort);
    }

    @Test
    @DisplayName("Debe lanzar error si el email no existe en la base de datos")
    void login_Fails_WhenEmailDoesNotExist() {
        //Arrange: La base de datos devuelve vacío
        when(userRepository.findByEmail("noexiste@test.com")).thenReturn(Mono.empty());

        //Act
        Mono<String> result = loginUseCase.login("noexiste@test.com", "12345");

        //Assert: Esperamos una RuntimeException con el mensaje específico
        StepVerifier.create(result)
                .expectErrorMessage("Credenciales invalidas")
                .verify();
    }

    @Test
    @DisplayName("Debe lanzar error si la contraseña es incorrecta")
    void login_Fails_WhenPasswordIsIncorrect() {
        //Arrange: El usuario existe
        User mockUser = new User("1", "hexuser", "existe@test.com", "hashed_pass", "bio");
        when(userRepository.findByEmail("existe@test.com")).thenReturn(Mono.just(mockUser));
        
        //Pero el PasswordEncoder dice que la contraseña NO coincide
        when(passwordEncoder.matches("clave_mala", "hashed_pass")).thenReturn(false);

        //Act
        Mono<String> result = loginUseCase.login("existe@test.com", "clave_mala");

        //Assert
        StepVerifier.create(result)
                .expectErrorMessage("Credenciales invalidas")
                .verify();
    }

    @Test
    @DisplayName("Debe devolver el JWT si las credenciales son correctas")
    void login_Success() {
        //Arrange
        User mockUser = new User("1", "hexuser", "existe@test.com", "hashed_pass", "bio");
        when(userRepository.findByEmail("existe@test.com")).thenReturn(Mono.just(mockUser));
        when(passwordEncoder.matches("clave_buena", "hashed_pass")).thenReturn(true);
        when(jwtPort.generateToken(mockUser)).thenReturn("eyJ.fake.jwt");

        //Act
        Mono<String> result = loginUseCase.login("existe@test.com", "clave_buena");

        //Assert
        StepVerifier.create(result)
                .expectNext("eyJ.fake.jwt") //Esperamos recibir el token
                .verifyComplete();
    }
}