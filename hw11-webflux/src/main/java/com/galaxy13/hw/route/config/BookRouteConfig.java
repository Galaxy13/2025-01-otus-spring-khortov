package com.galaxy13.hw.route.config;

import com.galaxy13.hw.route.handler.BookHandler;
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
public class BookRouteConfig {

    private final BookHandler handler;

    @Bean
    public RouterFunction<ServerResponse> routeBook() {
        return nest(RequestPredicates.path("/flux/book"), route()
                .GET("", handler::getAllBooks)
                .GET("/{id}", handler::getBookById)
                .POST("", handler::createBook)
                .PUT("/{id}", handler::updateBook)
                .DELETE("/{id}", handler::deleteBook)
                .build());
    }


}
