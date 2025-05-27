package com.galaxy13.hw.route.config;

import com.galaxy13.hw.route.handler.CommentHandler;
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
public class CommentRouteConfig {

    private final CommentHandler handler;

    @Bean
    public RouterFunction<ServerResponse> routeComment() {
        return nest(RequestPredicates.path("/flux/comment"), route()
                .GET("/{id}", handler::getCommentById)
                .GET("", handler::getCommentsByBookId)
                .POST("", handler::createComment)
                .PUT("/{id}", handler::editComment)
                .build());
    }
}
