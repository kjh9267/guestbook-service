package me.jun.guestbookservice.core.application;

import me.jun.guestbookservice.core.application.dto.PostResponse;
import me.jun.guestbookservice.core.domain.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static me.jun.guestbookservice.support.PostFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("deprecation")
public class PostServiceTest {

    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        postService = new PostService(postRepository);
    }

    @Test
    void createPostTest() {
        PostResponse expected = postResponse();

        given(postRepository.save(any()))
                .willReturn(post());

        assertThat(postService.createPost(Mono.just(createPostRequest())).block())
                .isEqualToComparingFieldByField(expected);
    }

    @Test
    void retrievePostTest() {
        PostResponse expected = postResponse();

        given(postRepository.findById(any()))
                .willReturn(Optional.of(post()));

        assertThat(postService.retrievePost(Mono.just(retrievePostRequest())).block())
                .isEqualToIgnoringGivenFields(expected);
    }

    @Test
    void retrievePostFailTest() {
        PostResponse expected = PostResponse.builder()
                .build();

        given(postRepository.findById(any()))
                .willReturn(Optional.empty());

        assertThat(postService.retrievePost(Mono.just(retrievePostRequest())).block())
                .isEqualToComparingFieldByField(expected);
    }
}
