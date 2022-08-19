package com.svydovets.bibirnate.exception;

public class EntityKeyNotPresentException extends RuntimeException {

    public EntityKeyNotPresentException(String message) {
        super(message);
    }

    public EntityKeyNotPresentException(String message, Throwable cause) {
        super(message, cause);
    }

}
