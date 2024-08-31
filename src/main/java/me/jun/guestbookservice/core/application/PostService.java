package me.jun.guestbookservice.core.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jun.guestbookservice.core.application.dto.*;
import me.jun.guestbookservice.core.application.exception.PostNotFoundException;
import me.jun.guestbookservice.core.domain.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Mono<PostResponse> createPost(Mono<CreatePostRequest> requestMono) {
        return requestMono.log()
                .map(CreatePostRequest::toEntity)
                .map(postRepository::save)
                .map(PostResponse::of)
                .doOnError(throwable -> log.info("{}", throwable));
    }

    public Mono<PostResponse> retrievePost(Mono<RetrievePostRequest> requestMono) {
        return requestMono.log()
                .map(request -> request.getId())
                .map(
                        id -> postRepository.findById(id)
                                .orElseThrow(() -> new PostNotFoundException(String.valueOf(id)))
                )
                .map(PostResponse::of)
                .doOnError(throwable -> log.info("{}", throwable));
    }

    public Mono<PostResponse> updatePost(Mono<UpdatePostRequest> requestMono) {
        return requestMono.log()
                .map(
                        request -> postRepository.findById(request.getId())
                                .map(post -> post.updateTitle(request.getTitle()))
                                .map(post -> post.updateContent(request.getContent()))
                                .orElseThrow(() -> new PostNotFoundException(String.valueOf(request.getId())))
        )
                .map(PostResponse::of)
                .doOnError(throwable -> log.info("", throwable));
    }

    public Mono<Void> deletePost(Mono<DeletePostRequest> requestMono) {
        return requestMono.log()
                .doOnNext(request -> postRepository.deleteById(request.getId()))
                .doOnError(throwable -> log.info("{}", throwable))
                .flatMap(request -> Mono.empty());
    }
}
