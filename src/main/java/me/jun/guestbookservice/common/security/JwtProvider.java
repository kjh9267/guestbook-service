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
        return jwtParser()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public void validateToken(String token) {
        try {
            jwtParser().parseClaimsJws(token);
        }
        catch (Exception e) {
            throw new InvalidTokenException(token);
        }
    }

    private JwtParser jwtParser() {
        return Jwts.parser()
                .setSigningKey(jwtKey)
                .build();
    }
}
