//follow/infrastructure/config/FollowUseCaseConfig.java
package com.hex.follow.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hex.follow.application.usecases.FollowUserUseCaseImpl;
import com.hex.follow.domain.ports.in.FollowUserUseCase;
import com.hex.follow.domain.ports.out.FollowRepositoryPort;
import com.hex.user.domain.ports.out.UserRepositoryPort;

/**
 * 
 * FollowUseCaseConfig: Clase de configuración para los casos de uso relacionados con la entidad Follow.
 * Esta clase se utiliza para definir y configurar los beans necesarios para los casos de uso de seguimiento de usuarios.
 */
@Configuration
public class FollowUseCaseConfig {
    
    @Bean
    public FollowUserUseCase followUserUseCase(FollowRepositoryPort followRepository, UserRepositoryPort userRepository) {
        return new FollowUserUseCaseImpl(followRepository, userRepository);
    }
}
