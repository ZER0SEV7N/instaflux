//post/infrastructure/adapters/out/storage/LocalImageStorageAdapter.java
package com.hex.post.infrastructure.adapters.out.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;

import com.hex.post.domain.ports.out.ImageStoragePort;

import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Mono;

/**
 * 
 * LocalImageStorageAdapter: Componente para el almacenamiento local de imágenes.
 * Esta clase proporciona la lógica para almacenar imágenes en un sistema de archivos local.
 */
@Component
public class LocalImageStorageAdapter implements ImageStoragePort {
    //Implementación de la interfaz ImageStoragePort para almacenamiento local de imágenes
    private final String UPLOAD_DIR = "uploads/"; //Directorio donde se almacenarán las imágenes
    private final Path basePath = Paths.get(UPLOAD_DIR); //Ruta base para el almacenamiento de imágenes

    @PostConstruct //PostConstruct se ejecuta después de que el bean ha sido construido y todas sus dependencias han sido inyectadas
    public void init() {
        try{
            Files.createDirectories(basePath); //Crea el directorio de almacenamiento si no existe
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio para imágenes", e); //Lanza una excepción si no se puede crear el directorio
        }
    }

    //Implementación del método uploadImage de la interfaz ImageStoragePort
    public Mono<String> uploadImage(FilePart file) {
        //Generar un nombre de archivo único para evitar colisiones
        String fileName = UUID.randomUUID().toString() + "-" + file.filename(); //Genera un nombre de archivo único usando UUID y el nombre original del archivo
        Path destination = basePath.resolve(fileName); //Resuelve la ruta completa del archivo de destino

        return file.transferTo(destination) //Transfiere el archivo al destino
                .then(Mono.just(fileName)); //Devuelve un Mono con el nombre del archivo almacenado   
    }
}
