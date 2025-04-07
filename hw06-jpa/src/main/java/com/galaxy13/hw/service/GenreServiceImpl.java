package com.galaxy13.hw.service;


import com.galaxy13.hw.dto.GenreDto;
import com.galaxy13.hw.repository.GenreRepository;
import com.galaxy13.hw.model.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.convert.converter.Converter;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    private final Converter<Genre, GenreDto> genreDtoConverter;

    @Transactional(readOnly = true)
    @Override
    public List<GenreDto> findAllGenres() {
        return genreRepository.findAllGenres().stream().map(genreDtoConverter::convert).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<GenreDto> findGenreById(long id) {
        return genreRepository.findGenreById(id).map(genreDtoConverter::convert);
    }

    @Transactional
    @Override
    public GenreDto saveGenre(long id, String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Genre name cannot be null or empty");
        }
        Genre genre = genreRepository.saveGenre(new Genre(id, name));
        return genreDtoConverter.convert(genre);
    }
}
