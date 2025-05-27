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

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    private final Converter<Genre, GenreDto> genreDtoMapper;

    @Transactional(readOnly = true)
    @Override
    public List<GenreDto> findAllGenres() {
        return genreRepository.findAll().stream().map(genreDtoMapper::convert).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public GenreDto findGenreById(long id) {
        return genreRepository.findById(id).map(genreDtoMapper::convert).orElseThrow(() ->
                new EntityNotFoundException("Genre with id %d not found".formatted(id)));
    }

    @Transactional
    @Override
    public GenreDto update(GenreUpsertDto genreDto) {
        long id = genreDto.id();
        Genre genre = genreRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Genre with id %d to update not exist".formatted(id)));
        genre.setName(genreDto.name());
        return genreDtoMapper.convert(genre);
    }

    @Transactional
    @Override
    public GenreDto create(GenreUpsertDto genreDto) {
        Genre genre = new Genre();
        genre.setName(genreDto.name());
        return genreDtoMapper.convert(genreRepository.save(genre));
    }
}
