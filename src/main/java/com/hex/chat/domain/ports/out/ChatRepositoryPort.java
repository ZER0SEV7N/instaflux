//chat/domain/ports/out/ChatRepositoryPort.java
package com.hex.chat.domain.ports.out;

import com.hex.chat.domain.model.ChatMessage;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interfaz que define los métodos para interactuar con el repositorio de chats.
 */
public interface ChatRepositoryPort {
    Mono<ChatMessage> save(ChatMessage chatMessage); //Guardar un mensaje de chat en el repositorio y devolver el mensaje guardado
    Flux<ChatMessage> findChatHistory(String user1, String user2); //Obtener el historial de chat entre dos usuarios y devolver una lista de mensajes de chat
}
