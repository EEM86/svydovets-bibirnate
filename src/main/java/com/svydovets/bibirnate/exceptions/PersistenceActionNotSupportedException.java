package com.svydovets.bibirnate.exceptions;

public class PersistenceActionNotSupportedException extends RuntimeException {
    public PersistenceActionNotSupportedException(String message) {
        super(message);
    }
}
