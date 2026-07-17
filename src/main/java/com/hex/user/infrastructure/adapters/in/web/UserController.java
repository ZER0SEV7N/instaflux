//user/domain/ports/in/UpdateProfileUseCase.java
package com.hex.user.infrastructure.adapters.in.web;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hex.global.infrastructure.web.ResponseGlobal;
import com.hex.user.domain.ports.in.UpdateProfileUseCase;
import com.hex.user.infrastructure.adapters.in.dto.UpdateBioRequest;
import com.hex.user.infrastructure.adapters.in.dto.UserProfileResponse;
import reactor.core.publisher.Mono;

/**
 * 
 * UserController: Controlador REST para manejar las solicitudes relacionadas con los usuarios.
 * Esta clase puede ser utilizada para definir endpoints y manejar las solicitudes HTTP relacionadas con los usuarios.
 */

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UpdateProfileUseCase updateProfileUseCase;

    public UserController(UpdateProfileUseCase updateProfileUseCase) {
        this.updateProfileUseCase = updateProfileUseCase;
    }

    /**
     * Endpoint para obtener el perfil de un usuario dado su correo electrónico.
     * GET: /api/users/{email}
     * @param email: Correo electrónico del usuario cuyo perfil se desea obtener.
     * @return: ResponseGlobal<UserProfileResponse>: Respuesta global con el estado de la operación y los datos del perfil del usuario.
     * @throws RuntimeException: Si no se encuentra el usuario con el correo electrónico proporcionado
     */
    @GetMapping("/{email}")
    public Mono<ResponseGlobal<UserProfileResponse>> getProfile(@PathVariable String email) {
        return updateProfileUseCase.getUserByEmail(email)
                .map(user -> new UserProfileResponse(user.username(), user.email(), user.bio()))
                .map(ResponseGlobal::success);
    }

    /**
     * Endpoint para obtener el perfil del usuario autenticado.
     * GET: /api/users/me
     * Obtiene el correo electrónico del usuario autenticado desde el contexto de seguridad y 
     * llama al caso de uso UpdateProfileUseCase para obtener los datos del perfil.
     * @param: Solamente tener JWT
     * @return: Username, email y bio del usuario autenticado.
     * @throws RuntimeException: Si no se puede obtener el perfil del usuario autenticado
     */
    @GetMapping("/profile/me")
    public Mono<ResponseGlobal<UserProfileResponse>> getMyProfile() {
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> context.getAuthentication().getName()) //Obtener el email del usuario autenticado desde el contexto de seguridad reactivo
                .flatMap(email -> updateProfileUseCase.getUserByEmail(email))
                .map(user -> new UserProfileResponse(user.username(), user.email(), user.bio()))
                .map(response -> ResponseGlobal.success(200, response, "Mi perfil"));
    }

    /**
     * Endpoint para actualizar la biografía del usuario autenticado.
     * PATCH: /api/users/me/bio
     * Obtiene el correo electrónico del usuario autenticado desde el contexto de seguridad y llama al caso de uso UpdateProfileUseCase para actualizar la biografía.
     * @param request: NewBio: "Nueva biografía" Nueva biografía proporcionada en el cuerpo de la solicitud.
     * @return: ResponseGlobal<UserProfileResponse>: Respuesta global con el estado de la operación y los datos actualizados del perfil del usuario.
     * @throws RuntimeException: Si no se puede actualizar la biografía del usuario
     */
    @PatchMapping("/profile/me/bio")
    public Mono<ResponseGlobal<UserProfileResponse>> updateBio(@RequestBody UpdateBioRequest request) {
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> context.getAuthentication().getName()) //Obtener el email del usuario autenticado desde el contexto de seguridad reactivo
                .flatMap(email -> updateProfileUseCase.updateBio(email, request.newBio()))
                .map(user -> new UserProfileResponse(user.username(), user.email(), user.bio()))
                .map(response -> ResponseGlobal.success(200, response, "Biografía actualizada"));
    }
}
