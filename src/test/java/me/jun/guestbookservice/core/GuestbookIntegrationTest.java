package me.jun.guestbookservice.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import redis.embedded.RedisServer;

import static io.restassured.RestAssured.given;
import static me.jun.guestbookservice.support.PostFixture.createPostRequest;
import static me.jun.guestbookservice.support.PostFixture.updatePostRequest;
import static me.jun.guestbookservice.support.RedisFixture.REDIS_PORT;
import static me.jun.guestbookservice.support.TokenFixture.createToken;
import static me.jun.guestbookservice.support.WriterFixture.WRITER_ID;
import static org.hamcrest.Matchers.hasKey;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@ActiveProfiles("test")
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.cloud.config.enabled=false"
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GuestbookIntegrationTest {

    @LocalServerPort
    private int port;

    private static String token;

    private RedisServer redisServer;

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    @BeforeEach
    void setUp() {
        token = createToken(WRITER_ID, 30L);
        redisServer = new RedisServer(REDIS_PORT);
        redisServer.start();
    }

    @AfterEach
    void tearDown() {
        redisServer.stop();
    }

    @Test
    void guestbookTest() {
        createPost();
        retrievePost(1L);
        updatePost();
        deletePost();
    }

    @Test
    void retrievePostListTest() {
        for (int count = 0; count < 10; count++) {
            createPost();
        }

        retrievePostList(0, 10);
    }

    private void createPost() {
        String response = given()
                .log().all()
                .port(port)
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, token)
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

    private void updatePost() {
        String response = given()
                .log().all()
                .port(port)
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, token)
                .body(updatePostRequest())

                .when()
                .put("/api/posts")

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

    private void deletePost() {
        String response = given()
                .log().all()
                .port(port)
                .header(AUTHORIZATION, token)

                .when()
                .delete("/api/posts/1")

                .then()
                .statusCode(OK.value())
                .extract()
                .asString();

        JsonElement element = JsonParser.parseString(response);
        System.out.println(gson.toJson(element));
    }

    private void retrievePostList(int page, int size) {
        String response = given()
                .log().all()
                .port(port)
                .accept(APPLICATION_JSON_VALUE)
                .queryParam("page", page)
                .queryParam("size", size)

                .when()
                .get("/api/posts/query")

                .then()
                .statusCode(OK.value())
                .body("$", x -> hasKey("postResponses"))
                .extract()
                .asString();

        JsonElement element = JsonParser.parseString(response);
        System.out.println(gson.toJson(element));
    }
}
