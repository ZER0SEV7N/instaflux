//global/infrastructure/config/CorsConfig.java
package com.hex.global.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * 
 * Archivo de configuración de CORS
 */
@Configuration
@EnableWebFlux
public class CorsConfig implements WebFluxConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") //Permite CORS en todos los endpoints
                .allowedOrigins("http://localhost:3000") //Permite solicitudes desde este origen
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") //Permite estos métodos HTTP
                .allowedHeaders("*")
                .allowCredentials(true); //Permite enviar cookies y credenciales
    }
    
}
