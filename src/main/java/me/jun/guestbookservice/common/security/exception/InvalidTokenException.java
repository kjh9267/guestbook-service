package me.jun.guestbookservice.common.security.exception;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(String token) {
        super(token);
    }
}
