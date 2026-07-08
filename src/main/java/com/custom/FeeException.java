package com.custom;
public class FeeException extends RuntimeException {

    public FeeException(String message) {
        super(message);
    }

    public FeeException(String message, Throwable cause) {
        super(message, cause);
    }
}