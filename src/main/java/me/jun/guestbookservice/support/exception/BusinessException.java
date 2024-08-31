package me.jun.guestbookservice.support.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
abstract public class BusinessException extends RuntimeException {

    protected HttpStatusCode status;

    protected BusinessException(String message) {
        super(message);
    }
}
