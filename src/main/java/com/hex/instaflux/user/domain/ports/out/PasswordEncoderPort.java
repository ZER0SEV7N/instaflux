//domain/ports/out/Password/EncoderPort.java
package com.hex.instaflux.user.domain.ports.out;

//Interfaz de salida para la codificación de contraseñas. Define los métodos que deben ser implementados por cualquier clase que se encargue de codificar y verificar contraseñas en el sistema.
public interface PasswordEncoderPort {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
