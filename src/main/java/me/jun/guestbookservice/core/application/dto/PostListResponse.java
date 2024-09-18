package me.jun.guestbookservice.core.application.dto;

import lombok.*;
import me.jun.guestbookservice.core.domain.Post;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class PostListResponse {

    private List<PostResponse> postResponses;

    public static PostListResponse of(List<Post> posts) {
        List<PostResponse> postResponseList = posts.stream()
                .map(PostResponse::of)
                .collect(Collectors.toList());

        return PostListResponse.builder()
                .postResponses(postResponseList)
                .build();
    }
}
