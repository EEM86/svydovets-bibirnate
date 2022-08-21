package com.svydovets.bibirnate.exception;

public class CacheOverloadException extends RuntimeException {

    public CacheOverloadException(String message) {
        super(message);
    }

    public CacheOverloadException(String message, Throwable cause) {
        super(message, cause);
    }

}
