package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.GenreDto;
import com.galaxy13.hw.dto.upsert.GenreUpsertDto;
import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.repository.GenreRepository;
import com.galaxy13.hw.model.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.convert.converter.Converter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    private final Converter<Genre, GenreDto> genreDtoMapper;

    @Override
    public Flux<GenreDto> findAllGenres() {
        return genreRepository.findAll()
                .mapNotNull(genreDtoMapper::convert);
    }

    @Override
    public Mono<GenreDto> findGenreById(String id) {
        return genreRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Genre not found")))
                .mapNotNull(genreDtoMapper::convert);
    }

    @Override
    public Mono<GenreDto> update(GenreUpsertDto genreDto) {
        return genreRepository.findById(genreDto.id())
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Genre to update not found")))
                .flatMap(genre -> {
                    genre.setName(genreDto.name());
                    return genreRepository.save(genre);
                }).mapNotNull(genreDtoMapper::convert);
    }

    @Transactional
    @Override
    public Mono<GenreDto> create(GenreUpsertDto genreDto) {
        Genre genre = new Genre();
        genre.setName(genreDto.name());
        return genreRepository.save(genre).mapNotNull(genreDtoMapper::convert);
    }
}
