package com.galaxy13.hw.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestControllerAdvice {

    @ExceptionHandler
    public void handleException(final Exception ex, final HttpServletRequest request) {
    }
}
