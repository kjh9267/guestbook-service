package me.jun.guestbookservice.common.exceptionhandler;

import me.jun.guestbookservice.support.exception.BusinessException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Mono<ErrorResponse> businessExceptionHandler(BusinessException e) {
        return Mono.fromSupplier(
                () -> ErrorResponse.builder(e, e.getStatus(), e.getMessage())
                            .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public Mono<ErrorResponse> exceptionHandler(Exception e) {
        return Mono.fromSupplier(
                () -> ErrorResponse.builder(e, INTERNAL_SERVER_ERROR, e.getMessage())
                        .build()
        );
    }
}
