//infrastructure/config/UserUseCaseConfig.java
package com.hex.user.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.hex.user.application.usecases.RegisterUseCaseImpl;
import com.hex.user.application.usecases.LoginUseCaseImpl;
import com.hex.user.domain.ports.in.LoginUseCase;
import com.hex.user.domain.ports.in.RegisterUseCase;
import com.hex.user.domain.ports.out.JwtPort;
import com.hex.user.domain.ports.out.PasswordEncoderPort;
import com.hex.user.domain.ports.out.UserRepositoryPort;

/**
 * 
 * UserUseCaseConfig: Ckase de ensablaje que configura y proporciona las dependencias necesarias para los casos de uso relacionados con los usuarios en la aplicación.
 * Esta clase se encarga de crear instancias de los casos de uso y sus dependencias
 * 
 */

@Configuration
public class UserUseCaseConfig {
    
    @Bean
    public RegisterUseCase registerUseCase(UserRepositoryPort userRepositoryPort, PasswordEncoderPort passwordEncoderPort) {
        return new RegisterUseCaseImpl(userRepositoryPort, passwordEncoderPort);
    }

    @Bean
    public LoginUseCase loginUserUseCase(UserRepositoryPort userRepositoryPort, PasswordEncoderPort passwordEncoderPort, JwtPort jwtPort) {
        return new LoginUseCaseImpl(userRepositoryPort, passwordEncoderPort, jwtPort);
    }
}
