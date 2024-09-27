package me.jun.guestbookservice.core.presentation;

import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jun.guestbookservice.common.security.WriterId;
import me.jun.guestbookservice.core.application.PostService;
import me.jun.guestbookservice.core.application.dto.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static io.netty.handler.codec.http.HttpHeaders.Values.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static reactor.core.scheduler.Schedulers.boundedElastic;

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
    @Timed(
            value = "posts.create",
            longTask = true
    )
    public Mono<ResponseEntity<PostResponse>> createPost(@RequestBody @Valid CreatePostRequest request, @WriterId Long writerId) {
        Mono<CreatePostRequest> requestMono = Mono.fromSupplier(
                () -> request.toBuilder()
                        .writerId(writerId)
                        .build()
                ).log()
                .publishOn(boundedElastic()).log();

        return postService.createPost(requestMono).log()
                .map(postResponse -> ResponseEntity.ok()
                        .body(postResponse)
                ).log()
                .doOnError(throwable -> log.error(throwable.getMessage()));
    }

    @GetMapping(
            value = "/{postId}",
            produces = APPLICATION_JSON
    )
    @Timed(
            value = "posts.retrieve",
            longTask = true
    )
    public Mono<ResponseEntity<PostResponse>> retrievePost(@PathVariable Long postId) {
        Mono<RetrievePostRequest> requestMono = Mono.fromSupplier(
                () -> RetrievePostRequest.of(postId)
                ).log()
                .publishOn(boundedElastic()).log();

        return postService.retrievePost(requestMono).log()
                .map(postResponse -> ResponseEntity.ok()
                                .body(postResponse)
                ).log()
                .doOnError(throwable -> log.error(throwable.getMessage()));
    }

    @PutMapping(
            consumes = APPLICATION_JSON,
            produces = APPLICATION_JSON
    )
    @Timed(
            value = "posts.update",
            longTask = true
    )
    public Mono<ResponseEntity<PostResponse>> updatePost(@RequestBody @Valid UpdatePostRequest request, @WriterId Long writerId) {
        Mono<UpdatePostRequest> requestMono = Mono.fromSupplier(
                () -> request.toBuilder()
                        .id(writerId)
                        .build()
                ).log()
                .publishOn(boundedElastic()).log();

        return postService.updatePost(requestMono).log()
                .map(postResponse -> ResponseEntity.ok()
                        .body(postResponse)
                ).log()
                .doOnError(throwable -> log.error(throwable.getMessage()));
    }

    @DeleteMapping("/{postId}")
    @Timed(
            value = "posts.delete",
            longTask = true
    )
    public Mono<ResponseEntity<Void>> deletePost(@PathVariable Long postId, @WriterId Long writerId) {
        Mono<DeletePostRequest> requestMono = Mono.fromSupplier(
                () -> DeletePostRequest.builder()
                        .id(postId)
                        .build()
                ).log()
                .publishOn(boundedElastic()).log();

        return postService.deletePost(requestMono).log()
                .map(empty -> ResponseEntity.ok()
                        .body(empty)
                ).log()
                .doOnError(throwable -> log.error(throwable.getMessage()));
    }

    @GetMapping(
            value = "/query",
            produces = APPLICATION_JSON_VALUE
    )
    @Timed(
            value = "posts.retrieveList",
            longTask = true
    )
    public Mono<ResponseEntity<PostListResponse>> retrievePostList(
            @RequestParam("page") int page,
            @RequestParam("size") int size
    ) {
        Mono<PageRequest> requestMono = Mono.fromSupplier(
                () -> PageRequest.of(page, size)
                ).log()
                .publishOn(boundedElastic()).log();

        return postService.retrievePostList(requestMono).log()
                .map(
                        response -> ResponseEntity.ok()
                                .body(response)
                ).log()
                .doOnError(throwable -> log.error(throwable.getMessage()));
    }
}
