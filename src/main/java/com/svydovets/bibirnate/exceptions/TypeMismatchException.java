package com.svydovets.bibirnate.exceptions;

/**
 * Exception thrown in case SQL type doesn't mache object type.
 */
public class TypeMismatchException extends RuntimeException {

    /**
     * Create a new exception {@code DefaultConstructorNotFoundException}.
     *
     * @param message the detailed message
     * @param cause   the root cause
     */
    public TypeMismatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
