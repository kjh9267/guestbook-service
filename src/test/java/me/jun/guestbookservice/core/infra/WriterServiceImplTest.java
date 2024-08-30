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

import java.io.IOException;

import static me.jun.guestbookservice.support.WriterFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
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
        mockWebServer.start(WRITER_BASE_URL_PORT);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void retrieveWriterIdByEmailTest() {
        MockResponse mockResponse = new MockResponse()
                .setHeader(CONTENT_TYPE, APPLICATION_JSON)
                .setBody(WRITER_RESPONSE_JSON);

        mockWebServer.url(WRITER_BASE_URL + ":" + mockWebServer.getPort() + WRITER_URI + "/" + WRITER_EMAIL);
        mockWebServer.enqueue(mockResponse);

        Object response = "" + writerServiceImpl.retrieveWriterIdByEmail(WRITER_EMAIL).block();

        assertThat(response)
                .isEqualTo(WRITER_RESPONSE);
    }
}