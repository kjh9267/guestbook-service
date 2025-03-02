package me.jun.guestbookservice.core.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jun.guestbookservice.core.application.dto.*;
import me.jun.guestbookservice.core.application.exception.PostNotFoundException;
import me.jun.guestbookservice.core.domain.Writer;
import me.jun.guestbookservice.core.domain.repository.PostRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
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
        return requestMono
                .map(CreatePostRequest::toEntity).log()
                .map(postRepository::save).log()
                .map(PostResponse::of);
    }

    public Mono<PostResponse> retrievePost(Mono<RetrievePostRequest> requestMono) {
        return requestMono
                .map(request -> request.getId()).log()
                .map(
                        id -> postRepository.findById(id)
                                .orElseThrow(() -> PostNotFoundException.of(String.valueOf(id)))
                ).log()
                .map(PostResponse::of);
    }

    public Mono<PostResponse> updatePost(Mono<UpdatePostRequest> requestMono) {
        return requestMono
                .map(
                        request -> postRepository.findById(request.getId())
                                .map(post -> post.updateTitle(request.getTitle()))
                                .map(post -> post.updateContent(request.getContent()))
                                .orElseThrow(() -> PostNotFoundException.of(String.valueOf(request.getId())))
                ).log()
                .map(PostResponse::of);
    }

    public Mono<Void> deletePost(Mono<DeletePostRequest> requestMono) {
        return requestMono
                .doOnNext(request -> postRepository.deleteById(request.getId())).log()
                .flatMap(request -> Mono.empty());
    }

    public Mono<PostListResponse> retrievePostList(Mono<PageRequest> requestMono) {
        return requestMono
                .map(request -> postRepository.findAllBy(request)).log()
                .map(PostListResponse::of);
    }

    @KafkaListener(
            topics = "member.delete",
            groupId = "guestbook-service"
    )
    public void deleteAllPostByWriter(
            @Payload Long writerId,
            Acknowledgment acknowledgment
    ) {
        Writer writer = Writer.builder()
                .value(writerId)
                .build();

        postRepository.deleteAllByWriter(writer);
        acknowledgment.acknowledge();
    }
}
