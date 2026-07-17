//post/domain/ports/out/ImageStoragePort.java
package com.hex.post.domain.ports.out;

import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

/**
 * 
 * ImageStoragePort: Interfaz para el almacenamiento de imágenes. 
 * Define los métodos que deben implementarse para almacenar y recuperar imágenes en un sistema de almacenamiento.
 */
public interface ImageStoragePort {
   
    Mono<String> uploadImage(FilePart filePart);
}
