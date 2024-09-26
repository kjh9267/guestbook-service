package me.jun.guestbookservice.core.application.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class RetrievePostRequest {

    @NotNull
    @Positive
    private Long id;

    public static RetrievePostRequest of(Long postId) {
        return RetrievePostRequest.builder()
                .id(postId)
                .build();
    }
}
