package me.jun.guestbookservice.common.exceptionhandler;

import lombok.extern.slf4j.Slf4j;
import me.jun.guestbookservice.support.exception.BusinessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Mono<ResponseEntity<ErrorResponse>> businessExceptionHandler(BusinessException e) {
        ErrorResponse errorResponse = ErrorResponse.of(e, e.getStatus(), e.getMessage());
        return Mono.fromSupplier(
                () -> ResponseEntity.status(e.getStatus())
                        .body(errorResponse)
        )
                .log()
                .doOnError(throwable -> log.error(throwable.getMessage()));
    }

    @ExceptionHandler({
            ServerWebInputException.class,
            ResponseStatusException.class,
    })
    public Mono<ResponseEntity<ErrorResponse>> bindExceptionHandler(Exception e) {
        ErrorResponse errorResponse = ErrorResponse.of(e, BAD_REQUEST, e.getMessage());
        return Mono.fromSupplier(
                () -> ResponseEntity.badRequest()
                        .body(errorResponse)
        )
                .log()
                .doOnError(throwable -> log.error(throwable.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> exceptionHandler(Exception e) {
        ErrorResponse errorResponse = ErrorResponse.of(e, INTERNAL_SERVER_ERROR, e.getMessage());
        return Mono.fromSupplier(
                () -> ResponseEntity.internalServerError()
                        .body(errorResponse)
        )
                .log()
                .doOnError(throwable -> log.error(throwable.getMessage()));
    }
}
