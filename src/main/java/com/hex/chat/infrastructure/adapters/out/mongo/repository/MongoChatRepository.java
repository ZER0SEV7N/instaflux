//chat/infrastructure/adapters/out/mongo/repository/MongoChatRepository.java
package com.hex.chat.infrastructure.adapters.out.mongo.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.hex.chat.infrastructure.adapters.out.mongo.entity.ChatMessageEntity;

import reactor.core.publisher.Flux;

/**
 * MongoChatRepository: Interfaz que define las operaciones de acceso a datos para los mensajes de chat en MongoDB.
 */
public interface MongoChatRepository extends ReactiveMongoRepository<ChatMessageEntity, String> {
    
    //Consulta para traer los mensajes donde user1 envió a user2, O user2 envió a user1, ordenados por fecha de creación Asc
    @Query("{ '$or': [ { 'senderEmail': ?0, 'receiverEmail': ?1 }, { 'senderEmail': ?1, 'receiverEmail': ?0 } ] }")
    Flux<ChatMessageEntity> findChatHistoryOrderByTimestampAsc(String user1, String user2);
}
