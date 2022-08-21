package com.svydovets.bibirnate.exception;

public class ParameterNotPresentException extends RuntimeException {

    public ParameterNotPresentException(String message) {
        super(message);
    }

    public ParameterNotPresentException(String message, Throwable cause) {
        super(message, cause);
    }

}
