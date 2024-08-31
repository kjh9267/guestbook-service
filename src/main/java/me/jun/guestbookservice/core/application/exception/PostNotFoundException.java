package me.jun.guestbookservice.core.application.exception;

import me.jun.guestbookservice.support.exception.BusinessException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class PostNotFoundException extends BusinessException {

    private PostNotFoundException(String message) {
        super(message);
        status = NOT_FOUND;
    }

    public static PostNotFoundException of(String message) {
        return new PostNotFoundException(message);
    }
}
