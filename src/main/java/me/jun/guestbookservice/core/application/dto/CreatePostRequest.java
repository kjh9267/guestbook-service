package me.jun.guestbookservice.core.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import me.jun.guestbookservice.core.domain.Post;
import me.jun.guestbookservice.core.domain.PostInfo;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class CreatePostRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    @Positive
    private Long writerId;

    public Post toEntity() {
        PostInfo postInfo = PostInfo.builder()
                .title(title)
                .content(content)
                .build();

        return Post.builder()
                .postInfo(postInfo)
                .writerId(writerId)
                .build();
    }
}