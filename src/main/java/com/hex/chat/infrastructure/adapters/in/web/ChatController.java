//chat/infrastructure/adapters/in/web/ChatController.java
package com.hex.chat.infrastructure.adapters.in.web;

import java.util.Map;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hex.chat.domain.model.ChatMessage;
import com.hex.chat.domain.ports.in.ChatUseCase;
import com.hex.global.infrastructure.web.ResponseGlobal;

import reactor.core.publisher.Mono;

/**
 * ChatController: Controlador para manejar las solicitudes HTTP relacionadas con el chat.
 * Esta clase se encarga de recibir las peticiones HTTP y coordinar la ejecución de los
 * casos de uso relacionados con el chat.
 */
@RestController
@RequestMapping("/api/chat")
public class ChatController {
    
    private final ChatUseCase chatUseCase;

    public ChatController(ChatUseCase chatUseCase) {
        this.chatUseCase = chatUseCase;
    }

    /**
     * PATCH: /api/chat/messages/{messageId}
     * Endpoint para editar un mensaje de chat existente.
     * Recibe el ID del mensaje a editar y el nuevo contenido en el cuerpo de la solicitud.
     * @REQUEST: JSON con el nuevo contenido del mensaje (content)
     * @RESPONSE: JSON con los datos del mensaje editado (id, senderEmail, receiverEmail, content, timestamp)
     */
    @PatchMapping("/messages/{messageId}")
    public Mono<ResponseGlobal<ChatMessage>> editMessage(@PathVariable String messageId, @RequestBody Map<String, String> body){
        return ReactiveSecurityContextHolder.getContext()
            .map(ctx -> ctx.getAuthentication().getName())
            .flatMap(email -> chatUseCase.editMessage(messageId, email, body.get("content")))
            .map(ResponseGlobal::success);
    }

    /**
     * DELETE: /api/chat/messages/{messageId}
     * Endpoint para eliminar un mensaje de chat existente.
     * Recibe el ID del mensaje a eliminar en la ruta de la solicitud.
     * @RESPONSE: JSON con un mensaje de éxito indicando que el mensaje fue eliminado
     */
    @DeleteMapping("/messages/{messageId}")
    public Mono<ResponseGlobal<Void>> deleteMessage(@PathVariable String messageId){
        return ReactiveSecurityContextHolder.getContext()
            .map(ctx -> ctx.getAuthentication().getName())
            .flatMap(email -> chatUseCase.deleteMessage(messageId, email))
            .thenReturn(ResponseGlobal.success(null, "Mensaje eliminado exitosamente"));
    }
}
