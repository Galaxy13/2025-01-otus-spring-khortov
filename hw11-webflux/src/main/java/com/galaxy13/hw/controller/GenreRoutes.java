package com.galaxy13.hw.controller;

import com.galaxy13.hw.dto.GenreDto;
import com.galaxy13.hw.dto.upsert.GenreUpsertDto;
import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Configuration
@RequiredArgsConstructor
public class GenreRoutes {

    private final GenreService genreService;

    @Bean
    public RouterFunction<ServerResponse> routeGenre() {
        return RouterFunctions.route()
                .GET("/flux/genre", this::getAllGenres)
                .GET("/flux/genre/{id}", this::getGenreById)
                .POST("/flux/genre", this::createGenre)
                .PUT("/flux/genre/{id}", this::updateGenre)

                .build();
    }

    private Mono<ServerResponse> getAllGenres(ServerRequest request) {
        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(genreService.findAllGenres(), GenreDto.class)
                .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    private Mono<ServerResponse> getGenreById(ServerRequest request) {
        String id = request.pathVariable("id");
        if (id.isBlank()) {
            return badRequest().contentType(MediaType.APPLICATION_JSON).build();
        }
        return genreService.findGenreById(id)
                .flatMap(genreDto -> ok().bodyValue(genreDto))
                .switchIfEmpty(notFound().build())
                .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    private Mono<ServerResponse> createGenre(ServerRequest request) {
        return request.bodyToMono(GenreUpsertDto.class)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Request body cannot be empty")))
                .flatMap(genreService::create)
                .flatMap(savedGenre -> created(URI.create("/genres/" + savedGenre.id()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedGenre))
                .onErrorResume(e -> badRequest().bodyValue(e.getMessage()));
    }

    private Mono<ServerResponse> updateGenre(ServerRequest request) {
        String id = request.pathVariable("id");
        if (id.isBlank()) {
            return badRequest().contentType(MediaType.APPLICATION_JSON).build();
        }

        return request.bodyToMono(GenreUpsertDto.class)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Request body cannot be empty")))
                .flatMap(genreService::update)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Genre to update not found")))
                .flatMap(updatedGenre -> ok().contentType(MediaType.APPLICATION_JSON)
                        .body(updatedGenre, GenreDto.class))
                .onErrorResume(e -> badRequest().bodyValue(e.getMessage()))
                .onErrorResume(e -> notFound().build());

    }
}
