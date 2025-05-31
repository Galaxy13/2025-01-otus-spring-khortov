package com.galaxy13.hw.exception.controller;

import com.galaxy13.hw.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            MismatchedIdsException.class
    })
    public ErrorResponse handleBadRequest(Exception e, WebRequest request) {
        if (e instanceof MethodArgumentNotValidException validationEx) {
            List<ErrorResponse.FieldErrorDetail> fieldErrors =
                    validationEx.getFieldErrors().stream().map(err ->
                            new ErrorResponse.FieldErrorDetail(err.getField(), err.getDefaultMessage())).toList();
            return ErrorResponse.createValidationError("Validation failed",
                    HttpStatus.BAD_REQUEST, request.getDescription(false), fieldErrors);
        }
        return ErrorResponse.create(e.getMessage(),
                HttpStatus.BAD_REQUEST, request.getDescription(false));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorResponse handleEntityNotFoundException(EntityNotFoundException e, WebRequest request) {
        return ErrorResponse.create(e.getMessage(), HttpStatus.NOT_FOUND, request.getDescription(false));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleInternalServerError(Exception ex, WebRequest request) {
        log.error("Internal Server Error", ex);
        return ErrorResponse.create("Internal Server error",
                HttpStatus.INTERNAL_SERVER_ERROR, request.getDescription(false));
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResponse handleAccessDeniedException(AccessDeniedException e, WebRequest request) {
        return ErrorResponse.create(e.getMessage(),
                HttpStatus.FORBIDDEN, request.getDescription(false));
    }
}
