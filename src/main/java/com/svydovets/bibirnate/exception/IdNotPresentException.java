package com.svydovets.bibirnate.exception;

public class IdNotPresentException extends RuntimeException {

    public IdNotPresentException(String message) {
        super(message);
    }

    public IdNotPresentException(String message, Throwable cause) {
        super(message, cause);
    }

}
