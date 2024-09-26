package me.jun.guestbookservice.core.application.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@Getter
public class UpdatePostRequest {

    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String content;
}
