//domain/ports/out/JwtPort.java
package com.hex.user.domain.ports.out;

import com.hex.user.domain.model.User;

//Interfaz de salida para la generación y validación de tokens JWT. Define los métodos que deben ser implementados por cualquier clase que se encargue de generar y validar tokens JWT en el sistema.
public interface JwtPort {
    String generateToken(User user);
    //boolean validateToken(String token);
}
