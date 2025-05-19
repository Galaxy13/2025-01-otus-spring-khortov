package com.galaxy13.hw.route.handler;

import com.galaxy13.hw.dto.AuthorDto;
import com.galaxy13.hw.dto.upsert.AuthorUpsertDto;
import com.galaxy13.hw.route.validator.UpsertDtoValidator;
import com.galaxy13.hw.service.AuthorService;
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
public class AuthorHandler {

    private final AuthorService authorService;

    private final UpsertDtoValidator validator;

    public Mono<ServerResponse> getAllAuthors(ServerRequest request) {
        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(authorService.findAllAuthors(), AuthorDto.class);
    }

    public Mono<ServerResponse> getAuthorById(ServerRequest request) {
        String id = request.pathVariable("id");
        return authorService.findAuthorById(id)
                .flatMap(author -> ok().bodyValue(author));
    }

    public Mono<ServerResponse> newAuthor(ServerRequest request) {
        return request.bodyToMono(AuthorUpsertDto.class).doOnNext(this::validate)
                .onErrorResume(e -> Mono.error(new IllegalArgumentException(e.getMessage())))
                .flatMap(authorService::create)
                .flatMap(savedAuthor -> created(URI.create("/author/" + savedAuthor.id()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedAuthor));
    }

    public Mono<ServerResponse> editAuthor(ServerRequest request) {
        String pathId = request.pathVariable("id");

        return request.bodyToMono(AuthorUpsertDto.class).doOnNext(this::validate)
                .onErrorResume(e -> Mono.error(new IllegalArgumentException(e.getMessage())))
                .flatMap(dto -> {
                    if (dto.id() != null && !dto.id().equals(pathId)) {
                        return Mono.error(new IllegalArgumentException("ID in path and body must match"));
                    }
                    return authorService.update(dto);
                })
                .flatMap(updatedAuthor -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(updatedAuthor));
    }

    private void validate(AuthorUpsertDto author) {
        Errors errors = new BeanPropertyBindingResult(author, "author");
        validator.validate(author, errors);
        if (errors.hasErrors()) {
            throw new ValidationException(errors.toString());
        }
    }
}
