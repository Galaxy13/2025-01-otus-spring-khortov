package com.galaxy13.hw.exception.controller;

import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(String message,
                            HttpStatus status,
                            Instant timestamp,
                            String path,
                            List<FieldErrorDetail> fieldErrors) {

    public static ErrorResponse create(String message, HttpStatus status, String path) {
        return new ErrorResponse(
                message,
                status,
                Instant.now(),
                path,
                List.of()
        );
    }

    public static ErrorResponse createValidationError(String message,
                                                      HttpStatus status,
                                                      String path, List<FieldErrorDetail> fieldErrors) {
        return new ErrorResponse(
                message,
                status,
                Instant.now(),
                path,
                fieldErrors
        );
    }

    public record FieldErrorDetail(String field, String message) {
    }
}
