//infrastructure/adapters/out/mongo/UserMongoAdapter.java
package com.hex.user.infrastructure.adapters.out.mongo;

import org.springframework.stereotype.Component;

import com.hex.user.domain.model.User;
import com.hex.user.domain.ports.out.UserRepositoryPort;
import com.hex.user.infrastructure.adapters.out.mongo.entity.UserEntity;
import com.hex.user.infrastructure.adapters.out.mongo.repository.MongoUserRepository;

import reactor.core.publisher.Mono;

/**
 * 
 * UserMongoAdapter: Clase adaptadora que implementa la interfaz UserRepository y se encarga de interactuar con la base de datos MongoDB para realizar operaciones CRUD sobre los usuarios.
 * Esta clase utiliza la interfaz SpringDataMongoUserRepository para acceder a los datos de los usuarios
 */

@Component
public class UserMongoAdapter implements UserRepositoryPort {
    
    private final MongoUserRepository repository;

    public UserMongoAdapter(MongoUserRepository repository) {
        this.repository = repository;
    }

    //Implementacion del metodo findByEmail
    public Mono<User> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(this::toDomain);
    }

    //Implementacion del metodo Save
    public Mono<User> save(User user){
        UserEntity entity = toEntity(user);
        return repository.save(entity)
                .map(this::toDomain);
    }

    //Implementacion del metodo findByUsername
    public Mono<Boolean> existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    //MAPPERS
    //Metodo para mapear de UserEntity a User
    private User toDomain(UserEntity entity) {
        return new User(entity.id(), entity.username(), entity.email(), entity.password(), entity.bio());
    }

    //Metodo para mapear de User a UserEntity
    private UserEntity toEntity(User user) {
        return new UserEntity(user.id(), user.username(), user.email(), user.password(), user.bio());
    }
    
}
