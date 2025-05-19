package com.galaxy13.hw.route.handler;

import com.galaxy13.hw.dto.CommentDto;
import com.galaxy13.hw.dto.upsert.CommentUpsertDto;
import com.galaxy13.hw.route.validator.UpsertDtoValidator;
import com.galaxy13.hw.service.CommentService;
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
import java.util.Optional;

import static org.springframework.web.reactive.function.server.ServerResponse.created;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@RequiredArgsConstructor
public class CommentHandler {

    private final CommentService commentService;

    private final UpsertDtoValidator validator;

    public Mono<ServerResponse> getCommentById(ServerRequest request) {
        String id = request.pathVariable("id");
        return commentService.findCommentById(id)
                .flatMap(commentDto -> ok().bodyValue(commentDto));
    }

    public Mono<ServerResponse> getCommentsByBookId(ServerRequest request) {
        Optional<String> bookId = request.queryParam("book_id");
        return bookId.map(id -> ok().contentType(MediaType.APPLICATION_JSON)
                        .body(commentService.findCommentByBookId(id), CommentDto.class))
                .orElseGet(() -> Mono.error(new IllegalArgumentException("Book id query parameter is required")));

    }

    public Mono<ServerResponse> createComment(ServerRequest request) {
        return request.bodyToMono(CommentUpsertDto.class).doOnNext(this::validate)
                .onErrorResume(e -> Mono.error(new IllegalArgumentException(e.getMessage())))
                .flatMap(commentService::create)
                .flatMap(savedComment -> created(URI.create("/comment/" + savedComment.id()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedComment));
    }

    public Mono<ServerResponse> editComment(ServerRequest request) {
        String id = request.pathVariable("id");

        return request.bodyToMono(CommentUpsertDto.class).doOnNext(this::validate)
                .onErrorResume(e -> Mono.error(new IllegalArgumentException(e.getMessage())))
                .flatMap(dto -> {
                    if (dto.id() != null && !dto.id().equals(id)) {
                        return Mono.error(new IllegalArgumentException("ID in path and body must match"));
                    }
                    return commentService.update(dto);
                })
                .flatMap(c -> ok().contentType(MediaType.APPLICATION_JSON).bodyValue(c));
    }

    private void validate(CommentUpsertDto comment) {
        Errors errors = new BeanPropertyBindingResult(comment, "comment");
        validator.validate(comment, errors);
        if (errors.hasErrors()) {
            throw new ValidationException(errors.toString());
        }
    }
}
