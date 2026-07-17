//chat/infrastructure/config/ChatConfig.java
package com.hex.chat.infrastructure.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import com.hex.chat.application.usecases.ChatUseCaseImpl;
import com.hex.chat.domain.ports.in.ChatUseCase;
import com.hex.chat.domain.ports.out.ChatRepositoryPort;
import com.hex.chat.infrastructure.adapters.in.websocket.ChatWebSocketHandler;

/**
 * ChatConfig: Clase de configuración para la funcionalidad de chat.
 * Esta clase puede ser utilizada para definir beans y configuraciones específicas para la funcionalidad de chat
 */
@Configuration
public class ChatConfig {

    //Bean para el caso de uso de chat
    @Bean
    public ChatUseCase chatUseCase(ChatRepositoryPort chatRepositoryPort) {
        return new ChatUseCaseImpl(chatRepositoryPort);
    }

    //Bean para el manejador de WebSocket de chat
    @Bean
    public HandlerMapping webSocketMapping(ChatWebSocketHandler chatWebSocketHandler){
        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("/ws/chat", chatWebSocketHandler);

        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setUrlMap(map);
        mapping.setOrder(-1); //Prioridad alta para que se maneje antes que otros mappings
        return mapping;
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
    
}
