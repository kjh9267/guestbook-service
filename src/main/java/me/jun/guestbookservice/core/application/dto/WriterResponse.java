package me.jun.guestbookservice.core.application.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class WriterResponse {

    private Long id;

    private String email;

    private String name;

    private String role;
}
