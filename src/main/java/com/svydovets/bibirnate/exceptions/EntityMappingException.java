package com.svydovets.bibirnate.exceptions;

import com.svydovets.bibirnate.annotation.Entity;

/**
 * Exception thrown in case there are some problems while converting sql to object or visa verse.
 */
public class EntityMappingException extends RuntimeException {

    /**
     * Create a new exception {@code DefaultConstructorNotFoundException}.
     *
     * @param message the detailed message
     */
    public EntityMappingException(String message) {
        super(message);
    }

    /**
     * Create a new exception {@code DefaultConstructorNotFoundException}.
     *
     * @param message the detailed message
     * @param cause   the root cause
     */
    public EntityMappingException(String message, Throwable cause) {
        super(message, cause);
    }
}
