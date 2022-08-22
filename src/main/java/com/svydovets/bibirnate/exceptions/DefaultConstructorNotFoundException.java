package com.svydovets.bibirnate.exceptions;

public class DefaultConstructorNotFoundException extends EntityMappingException {

    public DefaultConstructorNotFoundException(String message) {
        super(message);
    }

    public DefaultConstructorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
