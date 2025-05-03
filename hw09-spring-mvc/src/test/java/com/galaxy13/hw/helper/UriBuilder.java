package com.galaxy13.hw.helper;

import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

public class UriBuilder {
    private final UriComponentsBuilder uriBuilder;

    private UriBuilder(String uri) {
        uriBuilder = UriComponentsBuilder.newInstance();
        uriBuilder.path(uri);
    }

    public static UriBuilder fromStringUri(String uri) {
        return new UriBuilder(uri);
    }

    public UriBuilder queryParam(String paramName, Object paramValue) {
        if (paramValue instanceof Collection) {
            ((Collection<?>) paramValue).forEach(item ->
                    uriBuilder.queryParam(paramName, item));
        } else {
            uriBuilder.queryParam(paramName, paramValue);
        }
        return this;
    }

    public String build() {
        String uri = uriBuilder.toUriString();
        return URLDecoder.decode(uri, StandardCharsets.UTF_8);
    }
}
