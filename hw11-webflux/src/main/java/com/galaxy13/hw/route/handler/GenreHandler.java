package com.galaxy13.hw.route.handler;

import com.galaxy13.hw.dto.GenreDto;
import com.galaxy13.hw.dto.upsert.GenreUpsertDto;
import com.galaxy13.hw.route.validator.UpsertDtoValidator;
import com.galaxy13.hw.service.GenreService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
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

    private final UpsertDtoValidator validator;

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
        return request.bodyToMono(GenreUpsertDto.class).doOnNext(this::validate)
                .onErrorResume(e -> Mono.error(new IllegalArgumentException(e.getMessage())))
                .flatMap(genreService::create)
                .flatMap(savedGenre -> created(URI.create("/genre/" + savedGenre.id()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedGenre));
    }

    public Mono<ServerResponse> updateGenre(ServerRequest request) {
        String id = request.pathVariable("id");

        return request.bodyToMono(GenreUpsertDto.class).doOnNext(this::validate)
                .onErrorResume(e -> Mono.error(new IllegalArgumentException(e.getMessage())))
                .flatMap(dto -> {
                    if (dto.id() != null && !dto.id().equals(id)) {
                        return Mono.error(new IllegalArgumentException("ID in path and body must match"));
                    }
                    return genreService.update(dto);
                })
                .flatMap(updatedGenre -> ok().contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(updatedGenre));
    }

    private void validate(GenreUpsertDto genre) {
        Errors errors = new BeanPropertyBindingResult(genre, "genre");
        validator.validate(genre, errors);
        if (errors.hasErrors()) {
            throw new ValidationException(errors.toString());
        }
    }
}
