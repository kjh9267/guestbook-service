package me.jun.guestbookservice.common.security;

import me.jun.guestbookservice.common.security.exception.InvalidTokenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static me.jun.guestbookservice.common.security.TokenFixture.*;
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
        String email = jwtProvider.extractSubject(TOKEN);

        assertThat(email)
                .isEqualTo(EMAIL);
    }

    @Test
    void validateTokenTest() {
        assertThrows(
                InvalidTokenException.class,
                () -> jwtProvider.validateToken("wrong token")
        );
    }
}