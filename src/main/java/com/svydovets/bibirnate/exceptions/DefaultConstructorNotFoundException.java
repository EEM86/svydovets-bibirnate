package com.svydovets.bibirnate.exceptions;

import com.svydovets.bibirnate.annotation.Entity;

/**
 * Exception thrown when there's no default constructor defined in a class annotated with {@link Entity}.
 */
public class DefaultConstructorNotFoundException extends EntityMappingException {

    /**
     * Create a new exception {@code DefaultConstructorNotFoundException}.
     *
     * @param message the detailed message
     * @param cause   the root cause
     */
    public DefaultConstructorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
