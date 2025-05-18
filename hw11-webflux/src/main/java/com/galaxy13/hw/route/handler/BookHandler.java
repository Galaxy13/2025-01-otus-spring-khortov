package com.galaxy13.hw.route.handler;

import com.galaxy13.hw.dto.BookDto;
import com.galaxy13.hw.dto.upsert.BookUpsertDto;
import com.galaxy13.hw.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static org.springframework.web.reactive.function.server.ServerResponse.noContent;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@RequiredArgsConstructor
public class BookHandler {

    private final BookService bookService;

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
        return request.bodyToMono(BookUpsertDto.class)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Book request body has wrong format")))
                .flatMap(bookService::create)
                .flatMap(savedBook -> created(URI.create("/book/" + savedBook.id()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedBook));
    }

    public Mono<ServerResponse> updateBook(ServerRequest request) {
        String id = request.pathVariable("id");

        return request.bodyToMono(BookUpsertDto.class)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Book request body has wrong format")))
                .flatMap(dto -> {
                    if (!id.equals(dto.id())){
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
}
