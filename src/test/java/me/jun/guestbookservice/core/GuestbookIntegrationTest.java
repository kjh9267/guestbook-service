package me.jun.guestbookservice.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static me.jun.guestbookservice.support.PostFixture.createPostRequest;
import static me.jun.guestbookservice.support.WriterFixture.*;
import static org.hamcrest.Matchers.hasKey;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GuestbookIntegrationTest {

    @LocalServerPort
    private int port;

    private MockWebServer mockWebServer;

    private static final String TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhc2RmQGFzZGYuY29tIn0.-gGld1oGC_bjWSItm4t33Pd7NGSj7Kkb9nCiGNFqEc-qkdb14LXgioOFMLoYPMZjxB7icwbZZscNG3aP3zZ2Hw";

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

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
    void guestbookTest() {
        createPost();
        retrievePost(1L);
    }

    private void createPost() {
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(OK.value())
                .setHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .setBody(WRITER_RESPONSE_JSON);

        mockWebServer.url(WRITER_BASE_URL);
        mockWebServer.enqueue(mockResponse);

        String response = given()
                .log().all()
                .port(port)
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, TOKEN)
                .body(createPostRequest())

                .when()
                .post("/api/posts")

                .then()
                .statusCode(OK.value())
                .body("$", x -> hasKey("id"))
                .body("$", x -> hasKey("title"))
                .body("$", x -> hasKey("content"))
                .body("$", x -> hasKey("writerId"))
                .body("$", x -> hasKey("createdAt"))
                .body("$", x -> hasKey("updatedAt"))
                .extract()
                .asString();

        JsonElement element = JsonParser.parseString(response);
        System.out.println(gson.toJson(element));
    }

    private void retrievePost(Long id) {
        String response = given()
                .log().all()
                .port(port)
                .accept(APPLICATION_JSON_VALUE)

                .when()
                .get("/api/posts/" + id)

                .then()
                .statusCode(OK.value())
                .body("$", x -> hasKey("id"))
                .body("$", x -> hasKey("title"))
                .body("$", x -> hasKey("content"))
                .body("$", x -> hasKey("writerId"))
                .body("$", x -> hasKey("createdAt"))
                .body("$", x -> hasKey("updatedAt"))
                .extract()
                .asString();

        JsonElement element = JsonParser.parseString(response);
        System.out.println(gson.toJson(element));
    }
}
