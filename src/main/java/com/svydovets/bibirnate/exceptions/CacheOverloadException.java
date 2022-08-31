package com.svydovets.bibirnate.exceptions;

public class CacheOverloadException extends RuntimeException {

    public CacheOverloadException(String message) {
        super(message);
    }

    public CacheOverloadException(String message, Throwable cause) {
        super(message, cause);
    }

}
