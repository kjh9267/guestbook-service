package me.jun.guestbookservice.common.security.exception;

import me.jun.guestbookservice.support.exception.BusinessException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class InvalidTokenException extends BusinessException {

    public InvalidTokenException(String token) {
        super(token);
        status = UNAUTHORIZED;
    }

    public static InvalidTokenException of(String message) {
        return new InvalidTokenException(message);
    }
}
