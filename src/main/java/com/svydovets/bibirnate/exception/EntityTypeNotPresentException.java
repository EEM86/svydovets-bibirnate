package com.svydovets.bibirnate.exception;

public class EntityTypeNotPresentException extends RuntimeException {

    public EntityTypeNotPresentException(String message) {
        super(message);
    }

    public EntityTypeNotPresentException(String message, Throwable cause) {
        super(message, cause);
    }

}
