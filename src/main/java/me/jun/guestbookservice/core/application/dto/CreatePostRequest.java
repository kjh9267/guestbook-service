package me.jun.guestbookservice.core.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import me.jun.guestbookservice.core.domain.Post;
import me.jun.guestbookservice.core.domain.PostInfo;
import me.jun.guestbookservice.core.domain.Writer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@Getter
public class CreatePostRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private Long writerId;

    public Post toEntity() {
        PostInfo postInfo = PostInfo.builder()
                .title(title)
                .content(content)
                .build();

        Writer writer = Writer.builder()
                .value(writerId)
                .build();

        return Post.builder()
                .postInfo(postInfo)
                .writer(writer)
                .build();
    }
}
