package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.GenreDto;
import com.galaxy13.hw.dto.upsert.GenreUpsertDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GenreService {
    Flux<GenreDto> findAllGenres();

    Mono<GenreDto> findGenreById(String id);

    Mono<GenreDto> update(GenreUpsertDto genreDto);

    Mono<GenreDto> create(GenreUpsertDto genreDto);
}
