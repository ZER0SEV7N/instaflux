//infrastructure/adapters/out/security/JwtAdapter.java
package com.hex.user.infrastructure.adapters.out.security;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import javax.crypto.SecretKey;
import com.hex.user.domain.model.User;
import com.hex.user.domain.ports.out.JwtPort;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
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
    
    //Implementacion del getClaims que obtiene los claims del token JWT
    public Claims getClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    //Implementacion del metodo validateToken que valida el token JWT
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true; // Si no lanza excepción, la firma es correcta y no ha expirado
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    //Implementacion del metodo getEmailFromToken que obtiene el email del token JWT
    public String getEmailFromToken(String token) {
        return getClaims(token).getSubject();
    }
}

