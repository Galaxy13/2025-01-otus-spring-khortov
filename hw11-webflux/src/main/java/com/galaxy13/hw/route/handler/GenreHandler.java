package com.galaxy13.hw.route.handler;

import com.galaxy13.hw.dto.GenreDto;
import com.galaxy13.hw.dto.upsert.GenreUpsertDto;
import com.galaxy13.hw.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.server.ServerResponse.created;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@RequiredArgsConstructor
public class GenreHandler {

    private final GenreService genreService;

    public Mono<ServerResponse> getAllGenres(ServerRequest request) {
        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(genreService.findAllGenres(), GenreDto.class);
    }

    public Mono<ServerResponse> getGenreById(ServerRequest request) {
        String id = request.pathVariable("id");
        return genreService.findGenreById(id)
                .flatMap(genreDto -> ok().bodyValue(genreDto));
    }

    public Mono<ServerResponse> createGenre(ServerRequest request) {
        return request.bodyToMono(GenreUpsertDto.class)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Request body cannot be empty")))
                .flatMap(genreService::create)
                .flatMap(savedGenre -> created(URI.create("/genre/" + savedGenre.id()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedGenre));
    }

    public Mono<ServerResponse> updateGenre(ServerRequest request) {
        String id = request.pathVariable("id");

        return request.bodyToMono(GenreUpsertDto.class)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Request body cannot be empty")))
                .flatMap(dto -> {
                    if (dto.id() != null && !dto.id().equals(id)) {
                        return Mono.error(new IllegalArgumentException("ID in path and body must match"));
                    }
                    return genreService.update(dto);
                })
                .flatMap(updatedGenre -> ok().contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(updatedGenre));
    }
}
