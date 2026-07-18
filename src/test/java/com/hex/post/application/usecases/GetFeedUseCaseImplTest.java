//test/java/com/hex/post/application/usecases/GetFeedUseCaseImplTest.java
package com.hex.post.application.usecases;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.hex.follow.domain.model.Follow;
import com.hex.follow.domain.ports.out.FollowRepositoryPort;
import com.hex.post.domain.model.Post;
import com.hex.post.domain.ports.out.PostRepositoryPort;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * 
 * GetFeedUseCaseImplTest: Clase de prueba para GetFeedUseCaseImpl
 * Prueba unitaria para el caso de uso de obtener el feed de publicaciones
 */
class GetFeedUseCaseImplTest {
    
    private PostRepositoryPort postRepository;
    private FollowRepositoryPort followRepository;
    private GetFeedUseCaseImpl getFeedUseCase;

    @BeforeEach
    void setUp() {
        //Inicializar los Mocks antes de cada prueba
        postRepository = mock(PostRepositoryPort.class);
        followRepository = mock(FollowRepositoryPort.class);

        //Inicializar la clase que vamos a probar con los Mocks
        getFeedUseCase = new GetFeedUseCaseImpl(postRepository, followRepository);
    }

    @Test
    @DisplayName("Debe obtener el feed combinando los posts del usuario y los posts de los usuarios que sigue")
    void getFeed_Success() {
        //Arrange
        String myEmail = "me@myemail.com";

        //Simular que seguimos a dos usuarios
        Follow follow1 = new Follow("1", myEmail, "userA@test.com", Instant.now()); 
        Follow follow2 = new Follow("2", myEmail, "userB@test.com", Instant.now());
        when(followRepository.findByFollowerEmail(myEmail))
                .thenReturn(Flux.just(follow1, follow2));

        //Simular los posts de los usuarios
        Post post1 = new Post("100", "userA@test.com", "Contenido del post 1","url", 0, Instant.now());
        Post post2 = new Post("101", "me@myemail.com", "Contenido del post 2","url", 0, Instant.now());
        //Verigicar que el repositorio de posts devuelve los posts del usuario y de los usuarios que sigue
        when(postRepository.findByAuthorEmailInOrderByCreatedAtDesc(anyList()))
                .thenReturn(Flux.just(post1, post2));

        //Act
        Flux<Post> feed = getFeedUseCase.getFeed(myEmail);

        //Assert
        StepVerifier.create(feed)
                .expectNextMatches(post -> post.authorEmail().equals("userA@test.com"))
                .expectNextMatches(post -> post.authorEmail().equals("me@myemail.com"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe devolver un feed vacío si el usuario no sigue a nadie y no tiene posts")
    void getFeed_Empty_WhenNoFollowsAndNoPosts() {
        //Arrange
        String myEmail = "me@myemail.com";
        //Simular que el usuario no sigue a nadie
        when(followRepository.findByFollowerEmail(myEmail)).thenReturn(Flux.empty());
        //Simular que el usuario no tiene posts
        when(postRepository.findByAuthorEmailInOrderByCreatedAtDesc(anyList())).thenReturn(Flux.empty());
        //Act
        Flux<Post> feed = getFeedUseCase.getFeed(myEmail);
        //Assert
        StepVerifier.create(feed)
                .expectNextCount(0) //Esperar que no haya posts en el feed
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe obtener solo mis posts si no sigo a nadie")
    void getFeed_Success_WhenNotFollowingAnyone() {
        //Arrange
        String myEmail = "nuevo@test.com";

        //Simular que no seguimos a nadie
        when(followRepository.findByFollowerEmail(myEmail))
                .thenReturn(Flux.empty());

        //Simular la subida de un post por parte del usuario
        Post myPost = new Post("200", "nuevo@test.com", "¡Hola mundo!", "url", 0, Instant.now());
        
        when(postRepository.findByAuthorEmailInOrderByCreatedAtDesc(anyList()))
                .thenReturn(Flux.just(myPost));

        //Act
        Flux<Post> result = getFeedUseCase.getFeed(myEmail);

        //Assert: Aunque el Flux de Follows esté vacío, el código debe añadir mi email y buscar mis posts
        StepVerifier.create(result)
                .expectNextMatches(post -> post.content().equals("¡Hola mundo!"))
                .verifyComplete();
    }
}
