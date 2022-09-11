package com.svydovets.bibirnate.configuration.context;

import com.svydovets.bibirnate.exceptions.PersistenceContextAlreadyInitializedException;
import com.svydovets.bibirnate.exceptions.PersistenceContextNotInitializedException;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PersistenceContextProvider {
    private static PersistenceContext persistenceContext;

    public static PersistenceContext initializePersistenceContext(PersistenceContextBuilder builder) {
        if (persistenceContext != null) {
            throw new PersistenceContextAlreadyInitializedException();
        }
        return builder.build();
    }

    public static PersistenceContext getPersistenceContext() {
        if (persistenceContext == null) {
            throw new PersistenceContextNotInitializedException();
        }
        return persistenceContext;
    }
}
