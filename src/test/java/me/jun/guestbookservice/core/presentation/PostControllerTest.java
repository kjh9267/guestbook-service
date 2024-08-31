package me.jun.guestbookservice.core.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.jun.guestbookservice.core.application.PostService;
import me.jun.guestbookservice.core.application.exception.PostNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static me.jun.guestbookservice.support.PostFixture.postResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureWebTestClient
public class PostControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private PostService postService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void retrievePostTest() {
        given(postService.retrievePost(any()))
                .willReturn(Mono.just(postResponse()));

        webTestClient.get()
                .uri("/api/posts/1")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("id").exists()
                .jsonPath("title").exists()
                .jsonPath("content").exists()
                .jsonPath("writerId").exists()
                .jsonPath("createdAt").exists()
                .jsonPath("updatedAt").exists()
                .consumeWith(System.out::println);
    }

    @Test
    void retrievePostFailTest() {
        given(postService.retrievePost(any()))
                .willThrow(PostNotFoundException.of("1"));

        webTestClient.get()
                .uri("/api/posts/1")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody().jsonPath("detail").exists()
                .consumeWith(System.out::println);
    }
}
