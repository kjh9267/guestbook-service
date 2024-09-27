package me.jun.guestbookservice.core.infra;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import me.jun.guestbookservice.core.application.WriterService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.IOException;

import static me.jun.guestbookservice.support.WriterFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ActiveProfiles("test")
@SpringBootTest(properties = "spring.cloud.config.enabled=false")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class WriterServiceImplTest {

    @Autowired
    private WriterService writerServiceImpl;

    private MockWebServer mockWebServer;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start(WRITER_PORT);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void retrieveWriterIdByEmailTest() {
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(OK.value())
                .setHeader(CONTENT_TYPE, APPLICATION_JSON)
                .setBody(WRITER_RESPONSE_JSON);

        mockWebServer.url(WRITER_BASE_URL);
        mockWebServer.enqueue(mockResponse);

        Object response = writerServiceImpl.retrieveWriterIdByEmail(WRITER_EMAIL).block();

        assertThat(response)
                .isEqualTo(1L);
    }

    @Test
    void retrieveWriterIdByEmailFailTest() {
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(NOT_FOUND.value())
                .setHeader(CONTENT_TYPE, APPLICATION_JSON);

        mockWebServer.url(WRITER_BASE_URL);
        mockWebServer.enqueue(mockResponse);

        assertThrows(
                WebClientResponseException.class,
                () -> writerServiceImpl.retrieveWriterIdByEmail(WRITER_EMAIL).block()
        );
    }

    @Test
    void circuitBreakerTest() {
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(BAD_REQUEST.value());

        mockWebServer.url(WRITER_BASE_URL);

        for (int count = 0; count < 100; count++) {
            mockWebServer.enqueue(mockResponse);
            try {
                writerServiceImpl.retrieveWriterIdByEmail(WRITER_EMAIL)
                        .block();
            }
            catch (Exception e) {
                System.out.println(count + " " + e);
            }
        }

        mockWebServer.enqueue(mockResponse);
        assertThrows(
                CallNotPermittedException.class,
                () -> writerServiceImpl.retrieveWriterIdByEmail(WRITER_EMAIL).block()
        );
    }
}