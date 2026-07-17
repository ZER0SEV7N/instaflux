//follow/infrastructure/adapters/out/mongo/repository/MongoFollowRepository.java
package com.hex.follow.infrastructure.adapters.out.mongo.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.hex.follow.infrastructure.adapters.out.mongo.entity.FollowEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 
 * MongoFollowRepository: Interfaz que extiende ReactiveMongoRepository para la entidad FollowEntity. 
 * Proporciona métodos para realizar operaciones CRUD en la colección de seguimientos en la base de datos MongoDB.
 * Al extender ReactiveMongoRepository, esta interfaz hereda métodos para guardar, eliminar y buscar documentos de seguimiento de manera reactiva.
 */
public interface MongoFollowRepository extends ReactiveMongoRepository<FollowEntity, String> {
    Mono<Void> deleteByFollowerEmailAndFollowingEmail(String followerEmail, String followingEmail);
    Mono<Boolean> existsByFollowerEmailAndFollowingEmail(String followerEmail, String followingEmail);

    Flux<FollowEntity> findByFollowerEmail(String followerEmail);
    Flux<FollowEntity> findByFollowingEmail(String followingEmail);
}
