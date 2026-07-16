//src/main/java/com/hex/post/infrastructure/adapters/out/mongo/PostMongoAdapter.java
package com.hex.post.infrastructure.adapters.out.mongo;

import org.springframework.stereotype.Component;

import com.hex.post.domain.ports.out.PostRepositoryPort;
import com.hex.post.infrastructure.adapters.out.mongo.repository.MongoPostRepository;
import com.hex.post.domain.model.Post;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.hex.post.infrastructure.adapters.out.mongo.entity.PostEntity;

/**
 * 
 * PostMongoAdapter: Componente que actúa como adaptador para la entidad Post en la base de datos MongoDB.
 * Esta clase se encarga de interactuar con la base de datos MongoDB para realizar operaciones
 */
@Component
public class PostMongoAdapter implements PostRepositoryPort {
    
    private final MongoPostRepository repository;

    public PostMongoAdapter(MongoPostRepository repository) {
        this.repository = repository;
    }
    
    //Implementacion del metodo findAllByOrderByCreatedAtDesc
    public Flux<Post> findAllOrderByCreatedAtDesc() {
        return repository.findAllByOrderByCreatedAtDesc()
                .map(this::toDomain);
    }

    //Implementacion del metodo findByAuthorEmailOrderByCreatedAtDesc
    public Flux<Post> findByAuthorEmailOrderByCreatedAtDesc(String authorEmail) {
        return repository.findByAuthorEmailOrderByCreatedAtDesc(authorEmail)
                .map(this::toDomain);
    }
    
    //Implementacion del metodo save
    public Mono<Post> save(Post post) {
        return repository.save(toEntity(post))
                .map(this::toDomain);
    }

    //Implementacion del metodo findById
    public Mono<Post> findById(String postId) {
        return repository.findById(postId)
                .map(this::toDomain);
    }

    //Mappers
     private Post toDomain(PostEntity entity) {
        return new Post(entity.id(), entity.authorEmail(), entity.content(), entity.imageUrl(), entity.likes(), entity.createdAt());
    }

    private PostEntity toEntity(Post post) {
        return new PostEntity(post.id(), post.authorEmail(), post.content(), post.imageUrl(), post.likes(), post.createdAt());
    }
}
