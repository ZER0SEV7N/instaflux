//chat/infrastructure/adapters/in/websocket/ChatWebSocketHandler.java
package com.hex.chat.infrastructure.adapters.in.websocket;

import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hex.chat.domain.ports.in.ChatUseCase;
import com.hex.chat.infrastructure.adapters.in.dto.IncomingMessage;
import com.hex.user.infrastructure.adapters.out.security.JwtAdapter;

import reactor.core.publisher.Mono;

/**
 * 
 * ChatWebSocketHandler: Clase que maneja las conexiones WebSocket para la funcionalidad de chat en tiempo real.
 * Conecta a los clientes y permite la transmisión de mensajes de chat en vivo.
 */
public class ChatWebSocketHandler implements WebSocketHandler {

    private final ChatUseCase chatUseCase;
    private final JwtAdapter jwtAdapter;
    private final ObjectMapper objectMapper;

    public ChatWebSocketHandler(ChatUseCase chatUseCase, JwtAdapter jwtAdapter, ObjectMapper objectMapper) {
        this.chatUseCase = chatUseCase;
        this.jwtAdapter = jwtAdapter;
        this.objectMapper = objectMapper;
    }
    
    /**
     * handle: Método que maneja la conexión WebSocket. Se encarga de autenticar al usuario mediante JWT,
     * escuchar los mensajes entrantes y transmitir los mensajes de chat en tiempo real.
     * ws://localhost:8080/ws/chat?token=el.token.aqui
     * @param session: La sesión WebSocket que representa la conexión con el cliente.
     * @return: Mono<Void>: Representa la finalización de la operación de manejo de
     */
    public Mono<Void> handle(WebSocketSession session) {
        //Obtener el token JWT de la query string y validar el token
        String query = session.getHandshakeInfo().getUri().getQuery();
        if(query == null || !query.startsWith("token=")) return session.close();

        //Validar el token JWT
        String token = query.substring(6);
        if(!jwtAdapter.validateToken(token)) return session.close();

        String myEmail = jwtAdapter.getEmailFromToken(token);

        //Escuchar los mensajes entrantes del cliente y enviarlos al caso de uso de chat
        Mono<Void> input = session.receive()
            .map(msg -> msg.getPayloadAsText())
                .flatMap(json -> {
                    try {
                        //El frontend enviará un JSON tipo: { "receiverEmail": "userB@test.com", "content": "Hola!" }
                        IncomingMessage incoming = objectMapper.readValue(json, IncomingMessage.class);
                        return chatUseCase.sendMessage(myEmail, incoming.receiverEmail(), incoming.content());
                    } catch (Exception e) {
                        return Mono.empty();
                    }
                })
                .then();
        
        //Hablar: Escuchar los mensajes de chat en vivo y enviarlos al cliente
        Mono<Void> output = session.send(chatUseCase.listenToMyMessages(myEmail)
                .map(chatMessage -> {
                    try{
                        String jsonMsg = objectMapper.writeValueAsString(chatMessage);
                        return session.textMessage(jsonMsg);
                    }catch(Exception e){
                        return session.textMessage("{}");
                    }
                })
        );

        //Manejar la entrada y salida de mensajes de chat en paralelo
        return Mono.zip(input, output).then();
    }
}
