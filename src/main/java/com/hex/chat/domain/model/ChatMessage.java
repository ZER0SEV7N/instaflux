//chat/domain/model/ChatMessage.java
package com.hex.chat.domain.model;

import java.time.Instant;

/**
 * Record que representa un mensaje de chat.
 */
public record ChatMessage(
    String id, // Identificador único del mensaje
    String senderEmail, // Correo electrónico del remitente
    String receiverEmail, // Correo electrónico del receptor
    String content, // Contenido del mensaje
    Instant timestamp // Marca de tiempo del mensaje
) {   
}
