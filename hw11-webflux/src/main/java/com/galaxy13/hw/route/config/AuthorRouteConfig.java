package com.galaxy13.hw.route.config;

import com.galaxy13.hw.route.handler.AuthorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;

@Configuration
@RequiredArgsConstructor
public class AuthorRouteConfig {

    private final AuthorHandler handler;

    @Bean
    public RouterFunction<ServerResponse> routeAuthor() {
        return nest(RequestPredicates.path("/flux/author"), route()
                .GET("", handler::getAllAuthors)
                .GET("/{id}", handler::getAuthorById)
                .POST("", handler::newAuthor)
                .PUT("/{id}", handler::editAuthor)
                .build());
    }
}


