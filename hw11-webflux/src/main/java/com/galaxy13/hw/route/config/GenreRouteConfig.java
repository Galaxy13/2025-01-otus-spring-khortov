package com.galaxy13.hw.route.config;

import com.galaxy13.hw.route.handler.GenreHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;

@Configuration
@RequiredArgsConstructor
public class GenreRouteConfig {

    private final GenreHandler handler;

    @Bean
    public RouterFunction<ServerResponse> routeGenre() {
        return nest(RequestPredicates.path("/flux/genre"), route()
                .GET("", handler::getAllGenres)
                .GET("/{id}", handler::getGenreById)
                .POST("", handler::createGenre)
                .PUT("/{id}", handler::updateGenre)
                .build());
    }
}
