package com.svydovets.bibirnate.exception;

public class EntityNotPresentException extends RuntimeException {

    public EntityNotPresentException(String message) {
        super(message);
    }

    public EntityNotPresentException(String message, Throwable cause) {
        super(message, cause);
    }

}
