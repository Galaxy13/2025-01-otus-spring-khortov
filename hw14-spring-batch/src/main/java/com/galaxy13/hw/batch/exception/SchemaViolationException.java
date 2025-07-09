package com.galaxy13.hw.batch.exception;

public class SchemaViolationException extends RuntimeException {
    public SchemaViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}
