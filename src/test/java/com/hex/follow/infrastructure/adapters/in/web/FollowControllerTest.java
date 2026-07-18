//test/java/com/hex/follow/infrastructure/adapters/in/web/FollowControllerTest.java
package com.hex.follow.infrastructure.adapters.in.web;

import com.hex.follow.domain.ports.in.FollowUserUseCase;
import com.hex.global.infrastructure.web.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static org.mockito.Mockito.when;
/**
 * 
 * FollowControllerTest: Clase de prueba para FollowController
 * Prueba unitaria para el controlador de seguir y dejar de seguir usuarios
 */
class FollowControllerTest {

    private WebTestClient webTestClient;
    private FollowUserUseCase followUserUseCase;

    @BeforeEach
    void setUp() {
        //Mockear el caso de uso
        followUserUseCase = Mockito.mock(FollowUserUseCase.class);
        FollowController followController = new FollowController(followUserUseCase);

        //Crear un filtro de seguridad falso que inyecta un usuario autenticado en el contexto de seguridad
        //Esto evita que tengamos que levantar todo el contexto de Spring Security para el test
        WebFilter mockSecurityFilter = (exchange, chain) -> {
            Authentication auth = new UsernamePasswordAuthenticationToken("follower@test.com", null, Collections.emptyList());
            return chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
        };

        //Ensamblar el WebTestClient con el controlador, el manejador de errores y nuestro filtro falso
        webTestClient = WebTestClient.bindToController(followController)
                .controllerAdvice(new GlobalExceptionHandler())
                .webFilter(mockSecurityFilter)
                .build();
    }

    @Test
    @DisplayName("POST /api/users/{email}/follow - Debe seguir exitosamente y devolver 200")
    void followUser_Success() {
        //Arrange
        String targetEmail = "destino@test.com";
        //Recordar: follower@test.com es el correo que inyectamos en nuestro mockSecurityFilter
        when(followUserUseCase.follow("follower@test.com", targetEmail))
                .thenReturn(Mono.empty()); // El caso de uso devuelve Mono<Void> (vacío) al tener éxito

        //Act & Assert
        webTestClient.post()
                .uri("/api/users/" + targetEmail + "/follow")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.message").isEqualTo("Ahora sigues a destino@test.com");
    }

    @Test
    @DisplayName("DELETE /api/users/{email}/follow - Debe dejar de seguir exitosamente y devolver 200")
    void unfollowUser_Success() {
        //Arrange
        String targetEmail = "destino@test.com";
        when(followUserUseCase.unfollow("follower@test.com", targetEmail))
                .thenReturn(Mono.empty());

        //Act & Assert
        webTestClient.delete()
                .uri("/api/users/" + targetEmail + "/follow")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.message").isEqualTo("Ya no sigues a destino@test.com");
    }

    @Test
    @DisplayName("POST /api/users/{email}/follow - Debe atrapar error 400 si ya lo sigue")
    void followUser_Fails_WhenAlreadyFollowing() {
        //Arrange: Simulamos que el Caso de Uso lanza un error
        String targetEmail = "destino@test.com";
        when(followUserUseCase.follow("follower@test.com", targetEmail))
                .thenReturn(Mono.error(new RuntimeException("Ya sigues a este usuario")));

        //Act & Assert
        webTestClient.post()
                .uri("/api/users/" + targetEmail + "/follow")
                .exchange()
                //El GlobalExceptionHandler atrapa el RuntimeException y lo convierte en 400 Bad Request
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").isEqualTo("Ya sigues a este usuario");
    }
}