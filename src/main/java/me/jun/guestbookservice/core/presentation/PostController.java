package me.jun.guestbookservice.core.presentation;

import lombok.RequiredArgsConstructor;
import me.jun.guestbookservice.core.application.PostService;
import me.jun.guestbookservice.core.application.dto.PostResponse;
import me.jun.guestbookservice.core.application.dto.RetrievePostRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static io.netty.handler.codec.http.HttpHeaders.Values.APPLICATION_JSON;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping(
            value = "/{postId}",
            produces = APPLICATION_JSON
    )
    public Mono<ResponseEntity<PostResponse>> retrievePost(@PathVariable Long postId) {
        return postService.retrievePost(
                Mono.fromSupplier(
                        () -> RetrievePostRequest.of(postId))
        )
                .log()
                .map(
                        postResponse -> ResponseEntity.ok()
                                .body(postResponse)
        );
    }
}
