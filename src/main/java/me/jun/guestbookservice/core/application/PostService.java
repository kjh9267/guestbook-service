package me.jun.guestbookservice.core.application;

import lombok.RequiredArgsConstructor;
import me.jun.guestbookservice.core.application.dto.CreatePostRequest;
import me.jun.guestbookservice.core.application.dto.PostResponse;
import me.jun.guestbookservice.core.application.dto.RetrievePostRequest;
import me.jun.guestbookservice.core.domain.Post;
import me.jun.guestbookservice.core.domain.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Mono<PostResponse> createPost(Mono<CreatePostRequest> requestMono) {
        return requestMono.map(CreatePostRequest::toEntity)
                .map(postRepository::save)
                .map(PostResponse::of);
    }

    public Mono<PostResponse> retrievePost(Mono<RetrievePostRequest> requestMono) {
        return requestMono.map(request -> request.getId())
                .map(id -> postRepository.findById(id)
                            .orElse(Post.builder().build())
                )
                .map(PostResponse::of);
    }
}
