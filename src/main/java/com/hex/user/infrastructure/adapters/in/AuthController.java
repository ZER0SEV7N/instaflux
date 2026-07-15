//infrastructure/adapters/in/AuthController.java
package com.hex.user.infrastructure.adapters.in;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.hex.global.infrastructure.web.ResponseGlobal;
import com.hex.user.domain.ports.in.LoginUseCase;
import com.hex.user.domain.ports.in.RegisterUseCase;

import reactor.core.publisher.Mono;

/**
 * AuthController: Controlador que maneja las solicitudes relacionadas con la autenticación de usuarios en el sistema.
 * Esta clase se encarga de recibir las peticiones HTTP y coordinar la ejecución de los casos de uso correspondientes.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final RegisterUseCase registerUseCase;
    private final LoginUseCase loginUseCase;

    public AuthController(RegisterUseCase registerUseCase, LoginUseCase loginUseCase) {
        this.registerUseCase = registerUseCase;
        this.loginUseCase = loginUseCase;
    }
    
    /**
     * POST: /api/auth/register
     * Endpoint para registrar un nuevo usuario en el sistema. 
     * Recibe los datos del usuario en el cuerpo de la solicitud y delega la lógica de negocio al caso de uso correspondiente.
     * @REQUEST: JSON con los datos del usuario (username, email, password)
     * @RESPONSE: JSON con los datos del usuario registrado (id, username, email)
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseGlobal<UserResponse>> register(@RequestBody RegisterRequest request) {
        return registerUseCase.register(request.username(), request.email(), request.password())
                .map(user -> new UserResponse(user.id(), user.username(), user.email()))
                .map(response -> ResponseGlobal.success(201, response, "Usuario registrado exitosamente"));
    }

    /**
     * POST: /api/auth/login
     * Endpoint para iniciar sesión con un usuario existente.
     * Recibe las credenciales del usuario en el cuerpo de la solicitud y delega la lógica de negocio al caso de uso correspondiente.
     * @REQUEST: JSON con las credenciales del usuario (email, password)
     * @RESPONSE: JSON con el token de acceso y los datos del usuario (id, username, email, token)
     */
    @PostMapping("/login")
     public Mono<ResponseGlobal<AuthResponse>> login(@RequestBody LoginRequest request) {
        return loginUseCase.login(request.email(), request.password())
                .map(token -> ResponseGlobal.success(new AuthResponse(token), "Login exitoso"));
    }

    public record RegisterRequest(String username, String email, String password) {}
    public record LoginRequest(String email, String password) {}
    public record UserResponse(String id, String username, String email) {}
    public record AuthResponse(String token) {}
}
