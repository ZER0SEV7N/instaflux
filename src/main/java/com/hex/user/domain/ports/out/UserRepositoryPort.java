//domaincom.hex.user.domain.ports.out
package com.hex.user.domain.ports.out;

import com.hex.user.domain.model.User;

import reactor.core.publisher.Mono;

//Interfaz del repositorio de usuarios que define los métodos para interactuar con la capa de persistencia de datos. Esta interfaz es implementada por la clase UserRepositoryAdapter, que se encarga de realizar las operaciones CRUD sobre la entidad User en la base de datos.
public interface UserRepositoryPort {
    Mono<User> findByEmail(String email);
    Mono<User> save(User user);
    Mono<Boolean> existsByUsername(String username);
}
