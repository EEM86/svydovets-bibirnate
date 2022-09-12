package com.svydovets.bibirnate.exceptions;

public class TransactionManagerException extends Exception {

    public TransactionManagerException(String message) {
        super(message);
    }

    public TransactionManagerException(Throwable cause) {
        super(cause);
    }
}
