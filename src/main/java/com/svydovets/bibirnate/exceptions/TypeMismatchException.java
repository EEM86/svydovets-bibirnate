package com.svydovets.bibirnate.exceptions;

public class TypeMismatchException extends RuntimeException {

    public TypeMismatchException(String message) {
        super(message);
    }

    public TypeMismatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
