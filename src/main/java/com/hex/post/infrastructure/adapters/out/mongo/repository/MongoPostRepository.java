//post/infrastructure/adapters/out/mongo/repository/MongoPostRepository.java
package com.hex.post.infrastructure.adapters.out.mongo.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.hex.post.infrastructure.adapters.out.mongo.entity.PostEntity;
import reactor.core.publisher.Flux;

/**
 * 
 * MongoPostRepository: Interfaz que define las operaciones de acceso a datos para la entidad Post en la base de datos MongoDB.
 */
public interface MongoPostRepository extends ReactiveMongoRepository<PostEntity, String> {
    
    //Consulta para obtener todo por el orden de fecha de creación descendente
    Flux<PostEntity> findAllByOrderByCreatedAtDesc();
    //Consulta para obtener todos los posts de un autor específico por el orden de fecha de creación descendente
    Flux<PostEntity> findByAuthorEmailOrderByCreatedAtDesc(String authorEmail);
}
