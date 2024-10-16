package me.jun.guestbookservice.common.security;

import me.jun.guestbookservice.common.security.exception.InvalidTokenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static me.jun.guestbookservice.support.TokenFixture.JWT_KEY;
import static me.jun.guestbookservice.support.TokenFixture.createToken;
import static me.jun.guestbookservice.support.WriterFixture.WRITER_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtProviderTest {

    private JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {
        jwtProvider = new JwtProvider(JWT_KEY);
    }

    @Test
    void extractSubjectTest() {
        String token = createToken(WRITER_ID, 30L);
        String email = jwtProvider.extractSubject(token);

        assertThat(email)
                .isEqualTo(WRITER_ID.toString());
    }

    @Test
    void validateTokenTest() {
        assertThrows(
                InvalidTokenException.class,
                () -> jwtProvider.extractSubject("wrong token")
        );
    }

    @Test
    void validateExpiredTokenFailTest() {
        String token = createToken(WRITER_ID, 0L);

        assertThrows(
                InvalidTokenException.class,
                () -> jwtProvider.extractSubject(token)
        );
    }
}