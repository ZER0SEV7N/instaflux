//post/infrastructure/config/PostUseCaseConfig.java
package com.hex.post.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hex.follow.domain.ports.out.FollowRepositoryPort;
import com.hex.post.application.usecases.CreatePostUseCaseImpl;
import com.hex.post.application.usecases.GetFeedUseCaseImpl;
import com.hex.post.application.usecases.GetUserPostsUseCaseImpl;
import com.hex.post.application.usecases.LikePostUseCaseImpl;
import com.hex.post.domain.ports.in.CreatePostUseCase;
import com.hex.post.domain.ports.in.GetFeedUseCase;
import com.hex.post.domain.ports.in.GetUserPostsUseCase;
import com.hex.post.domain.ports.in.LikePostUseCase;
import com.hex.post.domain.ports.out.PostRepositoryPort;

/**
 * 
 * PostUseCaseConfig: Clase de configuración para los casos de uso relacionados con los posts.
 * Esta clase puede ser utilizada para definir beans y configuraciones específicas para los casos de uso de
 */
@Configuration
public class PostUseCaseConfig {
    
    @Bean
    public CreatePostUseCase createPostUseCase(PostRepositoryPort postRepositoryPort) {
        return new CreatePostUseCaseImpl(postRepositoryPort);
    }

    @Bean
    public GetFeedUseCase getFeedUseCase(PostRepositoryPort postRepository, FollowRepositoryPort followRepository) {
        return new GetFeedUseCaseImpl(postRepository, followRepository);
    }

    @Bean
    public LikePostUseCase likePostUseCase(PostRepositoryPort postRepositoryPort) {
        return new LikePostUseCaseImpl(postRepositoryPort);
    }

    @Bean
    public GetUserPostsUseCase getUserPostsUseCase(PostRepositoryPort postRepositoryPort) {
        return new GetUserPostsUseCaseImpl(postRepositoryPort);
    }

}
