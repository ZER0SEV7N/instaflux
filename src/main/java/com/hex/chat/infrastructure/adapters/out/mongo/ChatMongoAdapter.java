//chat/infrastructure/adapters/out/mongo/ChatMongoAdapter.java
package com.hex.chat.infrastructure.adapters.out.mongo;

import org.springframework.stereotype.Component;

import com.hex.chat.domain.model.ChatMessage;
import com.hex.chat.domain.ports.out.ChatRepositoryPort;
import com.hex.chat.infrastructure.adapters.out.mongo.entity.ChatMessageEntity;
import com.hex.chat.infrastructure.adapters.out.mongo.repository.MongoChatRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * ChatMongoAdapter: Adaptador para interactuar con la base de datos MongoDB para las operaciones de chat.
 */
@Component
public class ChatMongoAdapter implements ChatRepositoryPort {

    private final MongoChatRepository repository;

    public ChatMongoAdapter(MongoChatRepository repository) {
        this.repository = repository;
    }

    //Implementacion del metodo save para guardar un mensaje de chat en la base de datos MongoDB
    public Mono<ChatMessage> save(ChatMessage msg) {
        ChatMessageEntity entity = new ChatMessageEntity(null, msg.senderEmail(), msg.receiverEmail(), msg.content(), msg.timestamp());
        return repository.save(entity)
            .map(e -> new ChatMessage(e.id(), e.senderEmail(), e.receiverEmail(), e.content(), e.timestamp()));
    }

    //Implementacion del metodo findChatHistory para obtener el historial de chat entre dos usuarios
    public Flux<ChatMessage> findChatHistory(String user1, String user2) {
        return repository.findChatHistoryOrderByTimestampAsc(user1, user2)
            .map(e -> new ChatMessage(e.id(), e.senderEmail(), e.receiverEmail(), e.content(), e.timestamp()));
    }
    
}
