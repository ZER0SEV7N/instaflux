//global/infrastructure/config/WebFluxConfig.java
package com.hex.global.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * 
 * WebFluxConfig: Clase de configuración para WebFlux.
 * Esta clase permite configurar aspectos relacionados con WebFlux, como el manejo de recursos estáticos.
 */
@Configuration
public class WebFluxConfig implements WebFluxConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configura el manejo de recursos estáticos para servir imágenes desde la carpeta "uploads"
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}
