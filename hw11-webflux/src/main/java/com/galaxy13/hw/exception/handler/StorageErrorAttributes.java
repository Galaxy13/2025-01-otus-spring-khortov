package com.galaxy13.hw.exception.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.galaxy13.hw.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class StorageErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Throwable error = getError(request);

        Map<String, Object> errorAttributes = new HashMap<>() {{
            put("timestamp", Instant.now());
            put("message", error.getMessage());
            put("path", request.path());
        }};

        putErrorFields(error, errorAttributes);
        return errorAttributes;
    }

    private void putErrorFields(Throwable error, Map<String, Object> errorAttributes) {
        switch (error) {
            case EntityNotFoundException ignored -> {
                errorAttributes.put("status", HttpStatus.NOT_FOUND.value());
                errorAttributes.put("error", "Not Found");
            }
            case IllegalArgumentException ignored -> {
                errorAttributes.put("status", HttpStatus.BAD_REQUEST.value());
                errorAttributes.put("error", "Bad Request");
            }
            case JsonProcessingException ignored -> {
                errorAttributes.put("status", HttpStatus.BAD_REQUEST.value());
                errorAttributes.put("error", "Bad Request Body");
            }
            default -> {
                log.error("Internal error: { }", error);
                errorAttributes.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                errorAttributes.put("error", "Internal Server Error");
            }
        }
    }
}
