package me.jun.guestbookservice.support;

import io.jsonwebtoken.Jwts;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Date;

import static io.jsonwebtoken.SignatureAlgorithm.HS512;
import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.MINUTES;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
abstract public class TokenFixture {

    public static final String JWT_KEY = "1gnP47fOh0meRrSkU4n3ujd/CzNpGV3W9mBbgul+diJrQicNRavTYO5gYqluS/vsftHwyKZOFFtULf9cFVGsRuBG0ochG2LCHLuOuKvjkBJMz4xhimH+auDSA+jCdnuPyig6Ak6mF1u1Ovyck68Uc0qsebioLe7rpnF9lhPgCN4=";

    public static String createToken(Long id, Long expirationMinutes) {
        return Jwts.builder()
                .subject(id.toString())
                .issuedAt(Date.from(now()))
                .expiration(Date.from(now().plus(expirationMinutes, MINUTES)))
                .signWith(HS512, JWT_KEY)
                .compact();
    }
}
