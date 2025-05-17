package com.galaxy13.hw.controller;

import com.galaxy13.hw.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.RouterFunctions;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
public class CommentRoutes {

    private final CommentService commentService;

    @Bean
    public RouterFunction<ServerResponse> routeComment() {
        return RouterFunctions.route()
                .GET("/flux/comment/{id}",this::getCommentById)
                .GET("/flux/comment", this::getCommentsByBookId)
                .POST("/flux/comment", this::createComment)
                .PUT("/flux/comment/{id}", this::editComment)

                .build();
    }

    private Mono<ServerResponse> getCommentById(ServerRequest request) {
        return Mono.empty();
    }

    private Mono<ServerResponse> getCommentsByBookId(ServerRequest request) {
        return Mono.empty();
    }

    private Mono<ServerResponse> createComment(ServerRequest request) {
        return Mono.empty();
    }

    private Mono<ServerResponse> editComment(ServerRequest request) {
        return Mono.empty();
    }
}
