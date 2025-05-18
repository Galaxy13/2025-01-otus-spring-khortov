package com.galaxy13.hw.exception.handler;

import com.galaxy13.hw.exception.EntityNotFoundException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
public class StorageErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Throwable error = getError(request);

        Map<String, Object> errorAttributes = new HashMap<>(){{
            put("timestamp", Instant.now());
            put("message", error.getMessage());
            put("path", request.path());
        }};

        if (error instanceof EntityNotFoundException){
            errorAttributes.put("status", HttpStatus.NOT_FOUND.value());
            errorAttributes.put("error", "Not Found");
        } else if (error instanceof IllegalArgumentException) {
            errorAttributes.put("status", HttpStatus.BAD_REQUEST.value());
            errorAttributes.put("error", "Bad Request");
        } else {
            errorAttributes.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorAttributes.put("error", "Internal Server Error");
        }
        return errorAttributes;
    }
}
