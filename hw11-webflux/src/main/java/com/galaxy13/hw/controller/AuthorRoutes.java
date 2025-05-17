package com.galaxy13.hw.controller;

import com.galaxy13.hw.dto.AuthorDto;
import com.galaxy13.hw.dto.upsert.AuthorUpsertDto;
import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.created;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
@RequiredArgsConstructor
public class AuthorRoutes {

    private final AuthorService authorService;

    @Bean
    public RouterFunction<ServerResponse> routeAuthor() {
        return RouterFunctions.route()
                .GET("/flux/author", this::getAllAuthors)
                .GET("/flux/author/{id}", this::getAuthorById)
                .POST("/flux/author", this::newAuthor)
                .PUT("/flux/author/{id}", this::editAuthor)
                .build();
    }

    private Mono<ServerResponse> getAllAuthors(ServerRequest request) {
        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(authorService.findAllAuthors(), AuthorDto.class);
    }

    private Mono<ServerResponse> getAuthorById(ServerRequest request) {
        String id = request.pathVariable("id");
        if (id.isEmpty()) {
            return badRequest().build();
        }
        return authorService.findAuthorById(id)
                            .flatMap(author -> ok().bodyValue(author))
                            .switchIfEmpty(notFound().build())
                            .onErrorResume(e -> badRequest().bodyValue("Invalid ID format"));
    }

    private Mono<ServerResponse> newAuthor(ServerRequest request) {
        return request.bodyToMono(AuthorUpsertDto.class)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Request body cannot be empty")))
                .flatMap(authorService::create)
                .flatMap(savedAuthor -> created(URI.create("/authors/" + savedAuthor.id()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedAuthor))
                .onErrorResume(e -> badRequest().bodyValue(e.getMessage()));
    }

    private Mono<ServerResponse> editAuthor(ServerRequest request) {
        String id = request.pathVariable("id");
        if (id.isEmpty()) {
            return badRequest().bodyValue("id cannot be empty");
        }

        return request.bodyToMono(AuthorUpsertDto.class)
                            .flatMap(authorService::update)
                            .switchIfEmpty(Mono.error(new EntityNotFoundException("Author to update not found")))
                            .flatMap(updatedAuthor -> ok().contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(updatedAuthor))
                            .onErrorResume(e -> notFound().build());
    }
}


