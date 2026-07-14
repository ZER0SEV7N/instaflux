//domain/ports/out/JwtPort.java
package com.hex.instaflux.user.domain.ports.out;

//Interfaz de salida para la generación y validación de tokens JWT. Define los métodos que deben ser implementados por cualquier clase que se encargue de generar y validar tokens JWT en el sistema.
public interface JwtPort {
    String generateToken(String userId);
    boolean validateToken(String token);
    String extractUserId(String token);
}
