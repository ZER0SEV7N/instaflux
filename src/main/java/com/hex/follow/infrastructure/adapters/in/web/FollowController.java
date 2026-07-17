//follow/infrastructure/adapters/in/web/FollowController.java
package com.hex.follow.infrastructure.adapters.in.web;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hex.follow.domain.ports.in.FollowUserUseCase;
import com.hex.global.infrastructure.web.ResponseGlobal;

import reactor.core.publisher.Mono;

/**
 * 
 * FollowController: Clase que actúa como controlador REST para manejar las solicitudes relacionadas con la funcionalidad de seguimiento de usuarios.
 * Esta clase expone los endpoints de la API para permitir a los usuarios seguir y dejar de seguir a otros usuarios, 
 * delegando la lógica de negocio a la capa de casos de uso (FollowUserUseCase).
 */
@RestController
@RequestMapping("/api/users")
public class FollowController {
    
    private final FollowUserUseCase followUserUseCase;
    
    public FollowController(FollowUserUseCase followUserUseCase) {
        this.followUserUseCase = followUserUseCase;
    }

    /**
     * Endpoint para seguir a un usuario. Recibe el correo electrónico del usuario a seguir como parámetro de ruta.
     * Obtiene el correo electrónico del usuario que realiza la solicitud desde el contexto de seguridad y
     * llama al caso de uso FollowUserUseCase para realizar la operación de seguimiento.
     * POST /api/users/{targetEmail}/follow
     * @param targetEmail: Correo electrónico del usuario a seguir.
     * @return: Ahora sigues a {targetEmail} o un error si no se puede realizar la operación.
     */
    @PostMapping("/{targetEmail}/follow")
    public Mono<ResponseGlobal<String>> follow(@PathVariable String targetEmail) {
        return ReactiveSecurityContextHolder.getContext()
            .map(context -> context.getAuthentication().getName()) // Email de quien hace la petición
            .flatMap(followerEmail -> followUserUseCase.follow(followerEmail, targetEmail))
            // Al encadenarlo fuera del flatMap es más limpio. 
            // Usamos <String> para evitar que WebFlux elimine el cuerpo de la respuesta
            .thenReturn(ResponseGlobal.<String>success(null, "Ahora sigues a " + targetEmail));
    }

    /**
     * Endpoint para dejar de seguir a un usuario. Recibe el correo electrónico del usuario a dejar de seguir como parámetro de ruta.
     * Obtiene el correo electrónico del usuario que realiza la solicitud desde el contexto de seguridad y 
     * llama al caso de uso FollowUserUseCase para realizar la operación de dejar de seguir.
     * DELETE /api/users/{targetEmail}/follow
     * @param targetEmail: Correo electrónico del usuario a dejar de seguir.
     * @return: Ya no sigues a {targetEmail} o un error si no se puede realizar la operación.
     */
    @DeleteMapping("/{targetEmail}/follow")
    public Mono<ResponseGlobal<String>> unfollow(@PathVariable String targetEmail) {
        return ReactiveSecurityContextHolder.getContext()
            .map(context -> context.getAuthentication().getName()) 
            .flatMap(followerEmail -> followUserUseCase.unfollow(followerEmail, targetEmail))
            .thenReturn(ResponseGlobal.<String>success(null, "Ya no sigues a " + targetEmail));
    }
}
