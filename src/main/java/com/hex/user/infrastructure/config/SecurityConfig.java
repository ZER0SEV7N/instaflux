//infrastructure/config/SecurityConfig.java
package com.hex.user.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * 
 * SecurityConfig: Clase de configuración de seguridad que se encarga de definir la configuración de seguridad para la aplicación. 
 * Esta clase puede incluir configuraciones relacionadas con la autenticación, autorización, manejo de tokens JWT, entre otros aspectos de seguridad.
 * Actualmente, esta clase está vacía y no contiene ninguna configuración específica.
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
            //Deshabilitar CSRF para simplificar el desarrollo del Api Stateless
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            //Deshabilitar el login por formulario web clasico y la autenticacion basica HTTP
            .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
            .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
            //Configurar las rutas
            .authorizeExchange(exchanges -> exchanges
                //Las rutas de Auth son publicas
                .pathMatchers("/api/auth/register", "/api/auth/login").permitAll()
                .anyExchange().authenticated()
            )
            .build();
    }
}
