package me.jun.guestbookservice.core.application;

import me.jun.guestbookservice.core.application.dto.PostListResponse;
import me.jun.guestbookservice.core.application.dto.PostResponse;
import me.jun.guestbookservice.core.application.exception.PostNotFoundException;
import me.jun.guestbookservice.core.domain.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static me.jun.guestbookservice.support.PostFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        given(postRepository.findById(any()))
                .willReturn(Optional.empty());

        assertThrows(
                PostNotFoundException.class,
                () -> postService.retrievePost(Mono.just(retrievePostRequest())).block()
        );
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
        given(postRepository.findById(any()))
                .willThrow(PostNotFoundException.class);

        assertThrows(
                PostNotFoundException.class,
                () -> postService.updatePost(Mono.just(updatePostRequest())).block()
        );
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

    @Test
    void retrievePostListTest() {
        PostListResponse expected = postListResponse();

        given(postRepository.findAllBy(any()))
                .willReturn(postList());

        assertThat(postService.retrievePostList(Mono.just(PageRequest.of(0, 10))).block())
                .isEqualToComparingFieldByField(expected);
    }
}
