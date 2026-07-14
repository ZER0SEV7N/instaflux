//infrastructure/adapters/out/mongo/repository/SpringDataMongoUserRepository.java
package com.hex.user.infrastructure.adapters.out.mongo.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.hex.user.infrastructure.adapters.out.mongo.entity.UserEntity;

import reactor.core.publisher.Mono;

/**
 * 
 * SpringDataMongoUserRepository: Interfaz que define los métodos de acceso a la base de datos MongoDB para la entidad User. 
 * Esta interfaz es utilizada por la clase UserRepositoryAdapter para realizar operaciones CRUD sobre los usuarios en la base
 */
public interface SpringDataMongoUserRepository extends ReactiveMongoRepository<UserEntity, String> {
    
    //Spring genera la consulta a MongoDB automaticamente por el nombre del metodo
    Mono<UserEntity> findByEmail(String email);
    Mono<Boolean> existsByUsername(String username);
}
