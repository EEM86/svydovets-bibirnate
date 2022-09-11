package com.svydovets.bibirnate.exceptions;

public class PersistenceContextAlreadyInitializedException extends RuntimeException {

    private static final String MESSAGE = "Persistence context is already initialized.";

    public PersistenceContextAlreadyInitializedException() {
        super(MESSAGE);
    }
}
