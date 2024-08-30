package me.jun.guestbookservice.core.application.dto;

import lombok.*;
import me.jun.guestbookservice.core.domain.Post;

import java.time.Instant;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@Getter
public class PostResponse {

    private Long id;

    private String title;

    private String content;

    private Long writerId;

    private Instant createdAt;

    private Instant updatedAt;

    public static PostResponse of(Post post) {
        String title = post.getPostInfo() == null ? null: post.getPostInfo().getTitle();
        String content = post.getPostInfo() == null? null: post.getPostInfo().getContent();

        return PostResponse.builder()
                .id(post.getId())
                .title(title)
                .content(content)
                .writerId(post.getWriterId())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
