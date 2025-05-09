package me.jun.guestbookservice.core.application;

import lombok.extern.slf4j.Slf4j;
import me.jun.guestbookservice.core.application.dto.*;
import me.jun.guestbookservice.core.application.exception.PostNotFoundException;
import me.jun.guestbookservice.core.domain.Writer;
import me.jun.guestbookservice.core.domain.repository.PostRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;

    private final RedisService redisServiceImpl;

    private final int postSize;

    public PostService(
            PostRepository postRepository,
            RedisService redisService,
            @Value("${post-size}") int postSize
    ) {
        this.postRepository = postRepository;
        this.redisServiceImpl = redisService;
        this.postSize = postSize;
    }

    public Mono<PostResponse> createPost(Mono<CreatePostRequest> requestMono) {
        return requestMono
                .map(CreatePostRequest::toEntity).log()
                .map(postRepository::save).log()
                .publishOn(Schedulers.boundedElastic()).log()
                .doOnNext(post -> redisServiceImpl.deletePostList()).log()
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
                .publishOn(Schedulers.boundedElastic()).log()
                .doOnNext(post -> {
                    System.out.println(post.getId() + " " + postSize);
                    if (post.getId() <= postSize) {
                        redisServiceImpl.deletePostList();
                    }
                }).log()
                .map(PostResponse::of);
    }

    public Mono<Void> deletePost(Mono<DeletePostRequest> requestMono) {
        return requestMono
                .doOnNext(request -> postRepository.deleteById(request.getId())).log()
                .publishOn(Schedulers.boundedElastic()).log()
                .doOnNext(request -> {
                    if (request.getId() <= postSize) {
                        redisServiceImpl.deletePostList();
                    }
                }).log()
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
