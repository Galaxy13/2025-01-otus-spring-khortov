package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.service.GenreDto;
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

    @Override
    public GenreDto update(long id, String genreName) {
        if (genreName == null || genreName.isEmpty()) {
            throw new IllegalArgumentException("Genre name cannot be null or empty");
        }
        return genreDtoMapper.convert(genreRepository.save(new Genre(id, genreName)));
    }

    @Override
    public GenreDto insert(GenreDto genreDto) {
        return genreDtoMapper.convert(genreRepository.save(new Genre(genreDto.getId(), genreDto.getName())));
    }
}
