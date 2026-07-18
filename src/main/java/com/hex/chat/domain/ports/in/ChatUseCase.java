//chat/domain/ports/in/ChatUseCase.java
package com.hex.chat.domain.ports.in;

import com.hex.chat.domain.model.ChatMessage;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 
 * ChatUseCase: Interfaz de entrada para el caso de uso de chat
 * 
 */
public interface ChatUseCase {
    //Metodo para enviar un mensaje de chat entre dos usuarios
    Mono<ChatMessage> sendMessage(String senderEmail, String receiverEmail, String content);
    //Metodo para obtener el historial de chat entre dos usuarios
    Flux<ChatMessage> getChatHistory(String user1Email, String user2Email);
    //Metodo para escuchar los mensajes en vivo
    Flux<ChatMessage> listenToMyMessages(String userEmail);
    //Metodo para editar un mensaje de chat existente
    Mono<ChatMessage> editMessage(String messageId, String senderEmail, String newContent);
    //Metodo para eliminar un mensaje de chat existente
    Mono<Void> deleteMessage(String messageId, String senderEmail);
}
