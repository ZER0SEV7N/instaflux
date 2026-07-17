//chat/application/usecases/ChatUseCaseImp.java
package com.hex.chat.application.usecases;

import java.time.Instant;

import com.hex.chat.domain.model.ChatMessage;
import com.hex.chat.domain.ports.in.ChatUseCase;
import com.hex.chat.domain.ports.out.ChatRepositoryPort;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

/**
 * 
 * ChatUseCaseImp: Implementación de la interfaz ChatUseCase, que define los casos de uso relacionados con el chat.
 * Esta clase puede ser utilizada para implementar la lógica de negocio relacionada con el envío y recepción de
 */
public class ChatUseCaseImpl implements ChatUseCase{
    
    private final ChatRepositoryPort chatRepository;
    //Sinks.Many es un tipo de flujo que permite emitir múltiples elementos a múltiples suscriptores. 
    //En este caso, se utiliza para emitir mensajes de chat a todos los suscriptores interesados en recibirlos.
    private final Sinks.Many<ChatMessage> chatSink = Sinks.many().multicast().onBackpressureBuffer();

    public ChatUseCaseImpl(ChatRepositoryPort chatRepository) {
        this.chatRepository = chatRepository;
    }

    //Metodo para enviar un mensaje de chat entre dos usuarios
    public Mono<ChatMessage> sendMessage(String senderEmail, String receiverEmail, String content) {
        ChatMessage message = new ChatMessage(null, senderEmail, receiverEmail, content, Instant.now());
        
        return chatRepository.save(message)
            .doOnSuccess(savedMsg -> {
                chatSink.tryEmitNext(savedMsg);
            });
    }

    //Metodo para escuchar los mensajes en vivo
    public Flux<ChatMessage> listenToMyMessages(String userEmail) {
        return chatSink.asFlux()
                .filter(msg -> msg.senderEmail().equals(userEmail) || msg.receiverEmail().equals(userEmail));
    }

    //Metodo para obtener el historial de chat entre dos usuarios
    public Flux<ChatMessage> getChatHistory(String user1, String user2) {
        return chatRepository.findChatHistory(user1, user2);
    }
    
}
