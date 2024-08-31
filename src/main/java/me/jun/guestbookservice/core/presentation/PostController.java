package me.jun.guestbookservice.core.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jun.guestbookservice.common.security.WriterId;
import me.jun.guestbookservice.core.application.PostService;
import me.jun.guestbookservice.core.application.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static io.netty.handler.codec.http.HttpHeaders.Values.APPLICATION_JSON;

@Slf4j
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping(
            produces = APPLICATION_JSON,
            consumes = APPLICATION_JSON
    )
    public Mono<ResponseEntity<PostResponse>> createPost(@RequestBody @Valid CreatePostRequest request, @WriterId Long writerId) {
        return postService.createPost(
                Mono.fromSupplier(
                        () -> request.toBuilder()
                                .writerId(writerId)
                                .build()
                        )
                        .log()
        )
                .log()
                .map(postResponse -> ResponseEntity.ok()
                        .body(postResponse)
                )
                .doOnError(throwable -> log.info("{}", throwable));
    }

    @GetMapping(
            value = "/{postId}",
            produces = APPLICATION_JSON
    )
    public Mono<ResponseEntity<PostResponse>> retrievePost(@PathVariable Long postId) {
        return postService.retrievePost(
                Mono.fromSupplier(() -> RetrievePostRequest.of(postId)))
                .log()
                .map(postResponse -> ResponseEntity.ok()
                                .body(postResponse)
                )
                .doOnError(throwable -> log.info("{}", throwable));
    }

    @PutMapping(
            consumes = APPLICATION_JSON,
            produces = APPLICATION_JSON
    )
    public Mono<ResponseEntity<PostResponse>> updatePost(@RequestBody @Valid UpdatePostRequest request, @WriterId Long writerId) {
        return postService.updatePost(
                Mono.fromSupplier(
                        () -> request.toBuilder()
                                .id(writerId)
                                .build()
                )
                        .log()
        )
                .log()
                .map(postResponse -> ResponseEntity.ok()
                        .body(postResponse)
                )
                .doOnError(throwable -> log.info("{}", throwable));
    }

    @DeleteMapping("/{postId}")
    public Mono<ResponseEntity<Void>> deletePost(@PathVariable Long postId, @WriterId Long writerId) {
        return postService.deletePost(
                Mono.fromSupplier(
                        () -> DeletePostRequest.builder()
                                .id(postId)
                                .build()
                )
                        .log()
                        .doOnError(throwable -> log.info("{}", throwable))
        )
                .log()
                .map(empty -> ResponseEntity.ok()
                        .body(empty))
                .doOnError(throwable -> log.info("{}", throwable));
    }
}
