package me.jun.guestbookservice.core.infra;

import me.jun.guestbookservice.core.application.WriterService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.IOException;

import static me.jun.guestbookservice.support.WriterFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ActiveProfiles("test")
@SpringBootTest
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
}