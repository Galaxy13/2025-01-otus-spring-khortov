package com.galaxy13.hw.route.handler;

import com.galaxy13.hw.dto.BookDto;
import com.galaxy13.hw.dto.upsert.BookUpsertDto;
import com.galaxy13.hw.route.validator.UpsertDtoValidator;
import com.galaxy13.hw.service.BookService;
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

import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.created;
import static org.springframework.web.reactive.function.server.ServerResponse.noContent;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@RequiredArgsConstructor
public class BookHandler {

    private final BookService bookService;

    private final UpsertDtoValidator validator;

    public Mono<ServerResponse> getAllBooks(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(bookService.findAll(), BookDto.class);
    }

    public Mono<ServerResponse> getBookById(ServerRequest request) {
        String id = request.pathVariable("id");

        return bookService.findById(id)
                .flatMap(b -> ok().contentType(MediaType.APPLICATION_JSON).bodyValue(b));
    }

    public Mono<ServerResponse> createBook(ServerRequest request) {
        return request.bodyToMono(BookUpsertDto.class).doOnNext(this::validate)
                .onErrorResume(e -> Mono.error(new IllegalArgumentException(e.getMessage())))
                .flatMap(bookService::create)
                .flatMap(savedBook -> created(URI.create("/book/" + savedBook.id()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedBook));
    }

    public Mono<ServerResponse> updateBook(ServerRequest request) {
        String id = request.pathVariable("id");

        return request.bodyToMono(BookUpsertDto.class).doOnNext(this::validate)
                .onErrorResume(e -> Mono.error(new IllegalArgumentException(e.getMessage())))
                .flatMap(dto -> {
                    if (!id.equals(dto.id())) {
                        return badRequest().bodyValue("Path and body id mismatch");
                    }
                    return bookService.update(dto);
                })
                .flatMap(updatedBook -> ok().contentType(MediaType.APPLICATION_JSON).bodyValue(updatedBook));

    }

    public Mono<ServerResponse> deleteBook(ServerRequest request) {
        String id = request.pathVariable("id");

        return bookService.deleteById(id)
                .then(noContent().build());
    }

    private void validate(BookUpsertDto book) {
        Errors errors = new BeanPropertyBindingResult(book, "book");
        validator.validate(book, errors);
        if (errors.hasErrors()) {
            throw new ValidationException(errors.toString());
        }
    }
}
