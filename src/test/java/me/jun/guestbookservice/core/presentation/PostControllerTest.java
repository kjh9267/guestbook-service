package me.jun.guestbookservice.core.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.jun.guestbookservice.common.security.JwtProvider;
import me.jun.guestbookservice.common.security.exception.InvalidTokenException;
import me.jun.guestbookservice.core.application.PostService;
import me.jun.guestbookservice.core.application.WriterService;
import me.jun.guestbookservice.core.application.exception.PostNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import static me.jun.guestbookservice.support.PostFixture.*;
import static me.jun.guestbookservice.support.TokenFixture.EMAIL;
import static me.jun.guestbookservice.support.TokenFixture.TOKEN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureWebTestClient
public class PostControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private PostService postService;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private WriterService writerServiceImpl;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createPostTest() throws JsonProcessingException {
        String content = objectMapper.writeValueAsString(createPostRequest());

        given(postService.createPost(any()))
                .willReturn(Mono.just(postResponse()));

        given(jwtProvider.extractSubject(any()))
                .willReturn(EMAIL);

        given(writerServiceImpl.retrieveWriterIdByEmail(EMAIL))
                .willReturn(Mono.just(WRITER_ID));

        webTestClient.post()
                .uri("/api/posts")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .header(AUTHORIZATION, TOKEN)
                .bodyValue(content)
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
    void noContent_createPostFailTest() throws JsonProcessingException {
        webTestClient.post()
                .uri("/api/posts")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody().jsonPath("detail").exists()
                .consumeWith(System.out::println);
    }

    @Test
    void noToken_createPostFailTest() throws JsonProcessingException {
        String content = objectMapper.writeValueAsString(createPostRequest());

        webTestClient.post()
                .uri("/api/posts")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .bodyValue(content)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("detail").exists()
                .consumeWith(System.out::println);
    }

    @Test
    void invalidToken_createPostFailTest() throws JsonProcessingException {
        String content = objectMapper.writeValueAsString(createPostRequest());

        given(jwtProvider.extractSubject(any()))
                .willThrow(InvalidTokenException.of(TOKEN));

        webTestClient.post()
                .uri("/api/posts")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .header(AUTHORIZATION, TOKEN)
                .bodyValue(content)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("detail").exists()
                .consumeWith(System.out::println);
    }

    @Test
    void unknownWriter_createPostFailTest() throws JsonProcessingException {
        String content = objectMapper.writeValueAsString(createPostRequest());

        given(jwtProvider.extractSubject(any()))
                .willReturn(EMAIL);

        given(writerServiceImpl.retrieveWriterIdByEmail(any()))
                .willThrow(WebClientResponseException.class);

        webTestClient.post()
                .uri("/api/posts")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .header(AUTHORIZATION, TOKEN)
                .bodyValue(content)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .jsonPath("detail").exists()
                .consumeWith(System.out::println);
    }

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

    @Test
    void wrongPathVariable_retrievePostFailTest() {
        webTestClient.get()
                .uri("api/posts/asdf")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("detail").exists()
                .consumeWith(System.out::println);
    }

    @Test
    void updatePostTest() throws JsonProcessingException {
        String content = objectMapper.writeValueAsString(updatePostRequest());

        given(jwtProvider.extractSubject(any()))
                .willReturn(EMAIL);

        given(writerServiceImpl.retrieveWriterIdByEmail(any()))
                .willReturn(Mono.just(WRITER_ID));

        given(postService.updatePost(any()))
                .willReturn(Mono.just(postResponse()));

        webTestClient.put()
                .uri("/api/posts")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .header(AUTHORIZATION, TOKEN)
                .bodyValue(content)
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
    void noToken_updatePostFailTest() throws JsonProcessingException {
        String content = objectMapper.writeValueAsString(updatePostRequest());

        webTestClient.put()
                .uri("/api/posts")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .bodyValue(content)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("detail").exists()
                .consumeWith(System.out::println);
    }

    @Test
    void invalidToken_updatePostFailTest() throws JsonProcessingException {
        String content = objectMapper.writeValueAsString(updatePostRequest());

        given(jwtProvider.extractSubject(any()))
                .willThrow(InvalidTokenException.of(TOKEN));

        webTestClient.put()
                .uri("/api/posts")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .bodyValue(content)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("detail").exists()
                .consumeWith(System.out::println);
    }

    @Test
    void unknownWriter_updatePostFailTest() throws JsonProcessingException {
        String content = objectMapper.writeValueAsString(updatePostRequest());

        given(jwtProvider.extractSubject(any()))
                .willReturn(EMAIL);

        given(writerServiceImpl.retrieveWriterIdByEmail(any()))
                .willThrow(WebClientResponseException.class);

        webTestClient.put()
                .uri("/api/posts")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .header(AUTHORIZATION, TOKEN)
                .bodyValue(content)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .jsonPath("detail").exists()
                .consumeWith(System.out::println);
    }

    @Test
    void deletePostTest() {
        given(jwtProvider.extractSubject(any()))
                .willReturn(EMAIL);

        given(writerServiceImpl.retrieveWriterIdByEmail(any()))
                .willReturn(Mono.just(WRITER_ID));

        given(postService.deletePost(any()))
                .willReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/posts/1")
                .header(AUTHORIZATION, TOKEN)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(System.out::println);
    }

    @Test
    void wrongPathVariable_deletePostFailTest() {
        webTestClient.delete()
                .uri("/api/posts/asdf")
                .header(AUTHORIZATION, TOKEN)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("detail").exists()
                .consumeWith(System.out::println);
    }

    @Test
    void noPost_deletePostFailTest() {
        given(jwtProvider.extractSubject(any()))
                .willReturn(EMAIL);

        given(writerServiceImpl.retrieveWriterIdByEmail(any()))
                .willReturn(Mono.just(WRITER_ID));

        given(postService.deletePost(any()))
                .willThrow(PostNotFoundException.of("1"));

        webTestClient.delete()
                .uri("/api/posts/1")
                .header(AUTHORIZATION, TOKEN)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("detail").exists()
                .consumeWith(System.out::println);
    }

    @Test
    void noToken_deletePostFailTest() {
        webTestClient.delete()
                .uri("/api/posts/1")
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("detail").exists()
                .consumeWith(System.out::println);
    }

    @Test
    void invalidToken_deletePostFailTest() {
        given(jwtProvider.extractSubject(any()))
                .willThrow(InvalidTokenException.of(TOKEN));

        webTestClient.delete()
                .uri("/api/posts/1")
                .header(AUTHORIZATION, TOKEN)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("detail").exists()
                .consumeWith(System.out::println);
    }

    @Test
    void unknownWriter_deletePostFailTest() {
        given(jwtProvider.extractSubject(any()))
                .willReturn(EMAIL);

        given(writerServiceImpl.retrieveWriterIdByEmail(any()))
                .willThrow(WebClientResponseException.class);

        webTestClient.delete()
                .uri("/api/posts/1")
                .header(AUTHORIZATION, TOKEN)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .jsonPath("detail").exists()
                .consumeWith(System.out::println);
    }
}
