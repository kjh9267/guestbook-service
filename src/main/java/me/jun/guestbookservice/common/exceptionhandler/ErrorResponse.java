package me.jun.guestbookservice.common.exceptionhandler;

import lombok.*;
import org.springframework.http.HttpStatus;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class ErrorResponse {

    private Throwable throwable;

    private HttpStatus httpStatus;

    private String detail;

    public static ErrorResponse of(Throwable throwable, HttpStatus httpStatus, String detail) {
        return ErrorResponse.builder()
                .throwable(throwable)
                .httpStatus(httpStatus)
                .detail(detail)
                .build();
    }
}
