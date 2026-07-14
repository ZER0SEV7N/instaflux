//infrastructure/adapters/out/security/BcryptPasswordEncoderAdapter.java
package com.hex.user.infrastructure.adapters.out.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hex.user.domain.ports.out.PasswordEncoderPort;

/**
 * 
 * BcryptPasswordEncoderAdapter: Clase adaptadora que implementa la codificación de contraseñas utilizando el algoritmo Bcrypt. 
 * Esta clase se encarga de codificar y verificar contraseñas en el sistema.
 */
public class BcryptPasswordEncoderAdapter implements PasswordEncoderPort{
    
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    //Implementacion del metodo encode que codifica la contraseña en texto plano utilizando Bcrypt y devuelve la contraseña codificada.
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    //Implementacion del metodo matches que verifica si la contraseña en texto plano coincide con la contraseña codificada utilizando Bcrypt y devuelve un valor booleano.
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
