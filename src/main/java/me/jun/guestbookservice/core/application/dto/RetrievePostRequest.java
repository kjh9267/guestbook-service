package me.jun.guestbookservice.core.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

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
