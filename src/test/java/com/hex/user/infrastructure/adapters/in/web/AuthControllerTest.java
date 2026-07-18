//test/java/com/hex/user/infrastructure/adapters/in/web/AuthControllerTest.java
package com.hex.user.infrastructure.adapters.in.web;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.hex.global.infrastructure.web.GlobalExceptionHandler;
import com.hex.user.domain.model.User;
import com.hex.user.domain.ports.in.LoginUseCase;
import com.hex.user.domain.ports.in.RegisterUseCase;

import reactor.core.publisher.Mono;

import static org.mockito.Mockito.mock;
/**
 * 
 * AuthControllerTest: Clase de prueba para AuthController
 * Prueba unitaria para el controlador de autenticación
 */
class AuthControllerTest {
    
    //Herramienta para probar controladores web de Spring
    private WebTestClient webTestClient;

    //Declarar las dependencias del controlador que vamos a probar y sus Mocks
    private RegisterUseCase registerUseCase;
    private LoginUseCase loginUseCase;

    @BeforeEach
    void setUp() {
        //Inicializar los Mocks antes de cada prueba
        registerUseCase = mock(RegisterUseCase.class);
        loginUseCase = mock(LoginUseCase.class);
        AuthController authController = new AuthController(registerUseCase, loginUseCase);

        //Inicializar la herramienta para probar controladores web con el controlador que vamos a probar y sus dependencias
        webTestClient = WebTestClient.bindToController(authController)
                .controllerAdvice(new GlobalExceptionHandler()) // <-- ¡ESTA ES LA MAGIA!
                .build();
    }

    @Test
    @DisplayName("POST /api/auth/register - Debe registrar un nuevo usuario y devolver 201 CREATED")
    void register_Success(){
        //Arrenge: Simular el comportamiento del caso de uso
        User mockUser = new User("1", "Daniel", "daniel@example.com", "hashedpass", "Mi bio");
        when(registerUseCase.register(anyString(), anyString(), anyString()))
                .thenReturn(Mono.just(mockUser));
        
        AuthController.RegisterRequest request = new AuthController.RegisterRequest("Daniel", "daniel@example.com", "12345");

        //Act & Assert: Simular la petición HTTP y verificar la respuesta
        webTestClient.post()
                .uri("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()

                .expectStatus().isCreated() //Esperar que el código de estado sea 201 CREATED
                .expectBody()
                //Evaluar los campos del JSON devuelto en la respuesta
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.message").isEqualTo("Usuario registrado exitosamente")
                .jsonPath("$.data.email").isEqualTo("daniel@example.com")
                .jsonPath("$.data.username").isEqualTo("Daniel");
    }

    @Test
    @DisplayName("POST /api/auth/register - Debe devolver 400 BAD REQUEST si el email ya está registrado")
    void register_Fails_Returns400() {
        // Arrange: El caso de uso lanza el error esperado
        when(registerUseCase.register(anyString(), anyString(), anyString()))
                .thenReturn(Mono.error(new RuntimeException("El Email ya está registrado")));

        AuthController.RegisterRequest request = new AuthController.RegisterRequest("Daniel", "daniel@example.com", "12345");

        // Act & Assert: Simular la petición HTTP y verificar la respuesta
        webTestClient.post()
                .uri("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange() // Ejecuta la petición
                
                // Assert de Integración: Comprueba que el ControllerAdvice atrapó el error 
                // y lo convirtió correctamente en un status HTTP 400
                .expectStatus().isBadRequest() 
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").isEqualTo("El Email ya está registrado");
    }

    @Test
    @DisplayName("POST /api/auth/login - Debe iniciar sesión y devolver 200 OK con token")
    void login_Success() {
        //Arrenge: Simular el comportamiento del caso de uso
        String mockJwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.mockPayload.mockSignature";
        when(loginUseCase.login("daniel@example.com", "12345"))
                .thenReturn(Mono.just(mockJwt));

        AuthController.LoginRequest request = new AuthController.LoginRequest("daniel@example.com", "12345");

        //Act & Assert: Simular la petición HTTP y verificar la respuesta
        webTestClient.post()
                .uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()

                .expectStatus().isOk() //Esperar que el código de estado sea 200 OK
                .expectBody()
                //Evaluar los campos del JSON devuelto en la respuesta
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.message").isEqualTo("Login exitoso")
                .jsonPath("$.data.token").isEqualTo(mockJwt);
    }

    @Test
    @DisplayName("POST /api/auth/login - Debe devolver 401 UNAUTHORIZED si las credenciales son incorrectas")
    void login_Fails_Returns401() {
        // Arrange: El caso de uso lanza el error esperado
        when(loginUseCase.login("hex@test.com", "clave_falsa"))
                .thenReturn(Mono.error(new RuntimeException("Credenciales inválidas")));

        AuthController.LoginRequest request = new AuthController.LoginRequest("hex@test.com", "clave_falsa");

        // Act & Assert
        webTestClient.post()
                .uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange() // Ejecuta la petición
                
                // Assert de Integración: Comprueba que el ControllerAdvice atrapó el error 
                // y lo convirtió correctamente en un status HTTP 401
                .expectStatus().isUnauthorized() 
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").isEqualTo("Credenciales inválidas");
    }
}