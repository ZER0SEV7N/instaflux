//chat/infrastructure/adapters/out/mongo/entity/ChatMessageEntity.java
package com.hex.chat.infrastructure.adapters.out.mongo.entity;

import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
/**
 * 
 * ChatMessageEntity: Clase que representa la entidad de un mensaje de chat en la base de datos MongoDB.
 * 
 */

@Document(collection = "chat_messages")
public record ChatMessageEntity (
    @Id String id, // Identificador único del mensaje
    String senderEmail, // Correo electrónico del remitente
    String receiverEmail, // Correo electrónico del receptor
    String content, // Contenido del mensaje
    Instant timestamp // Marca de tiempo del mensaje
) {

}
