//infrastructure/adapters/out/security/JwtAdapter.java
package com.hex.user.infrastructure.adapters.out.security;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import javax.crypto.SecretKey;
import com.hex.user.domain.model.User;
import com.hex.user.domain.ports.out.JwtPort;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Jwts;
import java.util.Date;

/**
 * 
 * JwtAdapter: Clase adaptadora que implementa la interfaz JwtPort y se encarga de interactuar con la librería de JWT para generar y validar tokens JWT.
 * Esta clase utiliza la librería de JWT para generar y validar tokens JWT.
 */
@Component
public class JwtAdapter implements JwtPort {
    
    //Tomamos la clave secreta y el tiempo de expiración desde el application.properties
    //Si no existen, proveemos valores por defecto (útil para desarrollo rápido)
    @Value("${jwt.secret:defaultSecretKeyThatIsAtLeast32BytesLongForHS256Algorithm}")
    private String secret;

    @Value("${jwt.expiration:86400000}") 
    private long expiration; // 1 día en milisegundos

    //Implementacion del metodo generateToken
    public String generateToken(User user) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        
        return Jwts.builder()
                .subject(user.email())
                .claim("username", user.username()) 
                .claim("id", user.id())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }


}
