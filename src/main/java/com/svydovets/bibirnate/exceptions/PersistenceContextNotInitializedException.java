package com.svydovets.bibirnate.exceptions;

public class PersistenceContextNotInitializedException extends RuntimeException {

    private static final String MESSAGE = "Persistence context is not initialized. "
      + "Please use PersistenceContextProvider.initializePersistenceContext().";

    public PersistenceContextNotInitializedException() {
        super(MESSAGE);
    }
}
