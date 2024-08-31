package me.jun.guestbookservice.common.security;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import me.jun.guestbookservice.common.security.exception.InvalidTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("deprecation")
public class JwtProvider {

    private final String jwtKey;

    public JwtProvider(@Value("#{${jwt-key}}") String jwtKey) {
        this.jwtKey = jwtKey;
    }

    public String extractSubject(String token) {
        try {
            return jwtParser()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        }
        catch (Exception e) {
            throw InvalidTokenException.of(token);
        }
    }

    private JwtParser jwtParser() {
        return Jwts.parser()
                .setSigningKey(jwtKey)
                .build();
    }
}
