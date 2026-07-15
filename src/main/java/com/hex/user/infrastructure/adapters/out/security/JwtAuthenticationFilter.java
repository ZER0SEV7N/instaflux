//user/infrastructure/adapters/out/security/JwtAuthenticationFilter.java
package com.hex.user.infrastructure.adapters.out.security;

import org.springframework.http.HttpHeaders;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

/**
 * 
 * JwtAuthenticationFilter: Clase que implementa un filtro de autenticación para manejar la autenticación basada en tokens JWT en la aplicación.
 * Esta clase se encarga de interceptar las solicitudes entrantes, extraer el token JWT de la cabecera de autorización, 
 * validar el token y establecer la autenticación en el contexto de seguridad de Spring.
 * Actualmente, esta clase está vacía y no contiene ninguna implementación específica.
 */
@Component
public class JwtAuthenticationFilter implements WebFilter {
    
    private final JwtAdapter jwtAdapter;

    public JwtAuthenticationFilter(JwtAdapter jwtAdapter) {
        this.jwtAdapter = jwtAdapter;
    }

    //Implementación del método filter que intercepta las solicitudes entrantes y realiza la autenticación basada en tokens JWT.
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        //Extraer el token JWT de la cabecera de autorización
        String token = extractToken(exchange);

        if(token != null && jwtAdapter.validateToken(token)) {
            String email = jwtAdapter.getEmailFromToken(token);
            //Crear el objeto de Authentication y establecerlo en el contexto de seguridad de Spring
            Authentication auth = new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());

            // ¡Magia Reactiva! 
            // Continuamos la cadena de filtros PERO le inyectamos el contexto de seguridad.
            // contextWrite() permite pasar variables de contexto a través del flujo reactivo (Mono/Flux).
            return chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
        }
        //Si no hay token o el token no es válido, continuamos la cadena de filtros sin autenticación
        return chain.filter(exchange);
    }

    //Método auxiliar para extraer el token JWT de la cabecera de autorización
    private String extractToken(ServerWebExchange exchange) {
        String bearerToken = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Quitamos la palabra "Bearer "
        }
        return null;
    }
}
