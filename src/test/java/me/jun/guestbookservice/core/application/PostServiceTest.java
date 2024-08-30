package me.jun.guestbookservice.core.application;

import me.jun.guestbookservice.core.application.dto.PostResponse;
import me.jun.guestbookservice.core.domain.repository.PostRepository;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

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

    @Test
    void updatePostTest() {
        PostResponse expected = updatedPostResponse();

        given(postRepository.findById(any()))
                .willReturn(Optional.of(updatedPost()));

        assertThat(postService.updatePost(Mono.just(updatePostRequest())).block())
                .isEqualToComparingFieldByField(expected);
    }

    @Test
    void updatePostFailTest() {
        PostResponse expected = PostResponse.builder()
                .build();

        given(postRepository.findById(any()))
                .willReturn(Optional.empty());

        assertThat(postService.updatePost(Mono.just(updatePostRequest())).block())
                .isEqualToComparingFieldByField(expected);
    }

    @Test
    void deletePostTest() {
        doNothing()
                .when(postRepository)
                .deleteById(any());

        postService.deletePost(Mono.just(deletePostRequest()))
                .block();

        verify(postRepository)
                .deleteById(deletePostRequest().getId());
    }
}
