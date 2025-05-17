package com.galaxy13.hw.controller;

import com.galaxy13.hw.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
public class BookRoutes {

    private final BookService bookService;

    @Bean
    public RouterFunction<ServerResponse> routeBook() {
        return RouterFunctions.route()
                .GET("/flux/book", this::getAllBooks)
                .GET("/flux/book/{id}", this::getBookById)
                .POST("/flux/book", this::createBook)
                .PUT("/flux/book/{id}", this::updateBook)
                .DELETE("/flux/book/{id}", this::deleteBook)
                .build();
    }

    private Mono<ServerResponse> getAllBooks(ServerRequest request) {
        return Mono.empty();
    }

    private Mono<ServerResponse> getBookById(ServerRequest request) {
        return Mono.empty();
    }

    private Mono<ServerResponse> createBook(ServerRequest request) {
        return Mono.empty();
    }

    private Mono<ServerResponse> updateBook(ServerRequest request) {
        return Mono.empty();
    }

    private Mono<ServerResponse> deleteBook(ServerRequest request) {
        return Mono.empty();
    }
}
