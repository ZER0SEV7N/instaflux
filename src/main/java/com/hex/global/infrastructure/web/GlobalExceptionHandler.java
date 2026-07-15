//global/infrastructure/web/GlobalExceptionHandler.java
package com.hex.global.infrastructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import reactor.core.publisher.Mono;

/**
 * 
 * GlobalExceptionHandler: Clase de manejo global de excepciones que se encarga de capturar y manejar las excepciones que ocurren en la aplicación.
 * Esta clase puede incluir métodos para manejar diferentes tipos de excepciones y devolver respuestas HTTP adecuadas
 */
//@RestControllerAdvice: Anotación que indica que esta clase es un controlador de asesoramiento global para manejar excepciones en los controladores REST. 
//Permite capturar y manejar excepciones de manera centralizada, proporcionando respuestas HTTP consistentes y personalizadas para los errores que ocurren en la aplicación.
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    //Atrapamos las RuntimeException
    @ExceptionHandler(RuntimeException.class)
    public Mono<ResponseEntity<ResponseGlobal<Void>>> handleRuntimeException(RuntimeException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST; // Puedes ajustar el código de estado según tus necesidades

        if(ex.getMessage().contains("Credenciales")) 
            status = HttpStatus.UNAUTHORIZED; //401
        else if (ex.getMessage().contains("ya esta registrado"))
            status = HttpStatus.CONFLICT; //409
        else if (ex.getMessage().contains("no encontrado"))
            status = HttpStatus.NOT_FOUND; //404
        return Mono.just(
                ResponseEntity
                        .status(status)
                        .body(ResponseGlobal.error(status.value(), ex.getMessage()))
        );
    }

    //FallBack: Cualquier otra excepcion que no sea RuntimeException
    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ResponseGlobal<Void>>> handleException(Exception ex) {
        return Mono.just(
                ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ResponseGlobal.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error interno del servidor: " + ex.getMessage()))
        );
    }
}
