package me.jun.guestbookservice.common.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static me.jun.guestbookservice.support.TokenFixture.*;
import static me.jun.guestbookservice.support.WriterFixture.WRITER_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
                .isEqualTo(WRITER_ID.toString());
    }
}