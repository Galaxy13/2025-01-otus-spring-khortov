package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.AuthorDto;
import com.galaxy13.hw.dto.upsert.AuthorUpsertDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AuthorService {
    Flux<AuthorDto> findAllAuthors();

    Mono<AuthorDto> findAuthorById(String id);

    Mono<AuthorDto> create(AuthorUpsertDto newAuthor);

    Mono<AuthorDto> update(AuthorUpsertDto updatedAuthor);
}
