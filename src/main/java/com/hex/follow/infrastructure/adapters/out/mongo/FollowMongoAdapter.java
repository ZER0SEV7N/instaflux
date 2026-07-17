//follow/infrastructure/adapters/out/mongo/FollowMongoAdapter.java
package com.hex.follow.infrastructure.adapters.out.mongo;

import org.springframework.stereotype.Component;

import com.hex.follow.domain.model.Follow;
import com.hex.follow.domain.ports.out.FollowRepositoryPort;
import com.hex.follow.infrastructure.adapters.out.mongo.entity.FollowEntity;
import com.hex.follow.infrastructure.adapters.out.mongo.repository.MongoFollowRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 
 * FollowMongoAdapter: Adaptador que implementa la interfaz FollowRepositoryPort para interactuar con la base de datos MongoDB.
 * Esta clase se utiliza para realizar operaciones de persistencia y recuperación de datos relacionados con el seguimiento entre usuarios.
 */
@Component
public class FollowMongoAdapter implements FollowRepositoryPort {
    
    private final MongoFollowRepository repository;

    public FollowMongoAdapter(MongoFollowRepository repository) {
        this.repository = repository;
    }

    //Metodo para guardar un follow en la base de datos
    public Mono<Follow> save(Follow follow){
        FollowEntity entity = new FollowEntity(follow.id(), follow.followerEmail(), follow.followingEmail(), follow.createdAt());
        return repository.save(entity)
                .map(saved -> new Follow(saved.id(), saved.followerEmail(), saved.followingEmail(), saved.createdAt()));
    }

    //Metodo para eliminar un follow de la base de datos
    public Mono<Void> deleteByFollowerAndFollowing(String followerEmail, String followingEmail){
        return repository.deleteByFollowerEmailAndFollowingEmail(followerEmail, followingEmail);
    }

    //Metodo para buscar si existe un follow entre dos usuarios
    public Mono<Boolean> existsByFollowerAndFollowing(String followerEmail, String followingEmail){
        return repository.existsByFollowerEmailAndFollowingEmail(followerEmail, followingEmail);
    }

    //Metodo para obtener listas de seguidores y seguidos
    public Flux<Follow> findByFollowerEmail(String followerEmail) {
        return repository.findByFollowerEmail(followerEmail)
                .map(entity -> new Follow(entity.id(), entity.followerEmail(), entity.followingEmail(), entity.createdAt()));
    }

    //Metodo para obtener listas de seguidores y seguidos
    public Flux<Follow> findByFollowingEmail(String followingEmail) {
        return repository.findByFollowingEmail(followingEmail)
                .map(entity -> new Follow(entity.id(), entity.followerEmail(), entity.followingEmail(), entity.createdAt()));
    }
}
