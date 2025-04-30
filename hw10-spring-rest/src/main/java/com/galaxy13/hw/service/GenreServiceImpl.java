package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.request.GenreRequestDto;
import com.galaxy13.hw.dto.response.GenreResponseDto;
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

    private final Converter<Genre, GenreResponseDto> genreDtoMapper;

    @Transactional(readOnly = true)
    @Override
    public List<GenreResponseDto> findAllGenres() {
        return genreRepository.findAll().stream().map(genreDtoMapper::convert).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public GenreResponseDto findGenreById(long id) {
        return genreRepository.findById(id).map(genreDtoMapper::convert).orElseThrow(() ->
                new EntityNotFoundException("Genre with id %d not found".formatted(id)));
    }

    @Transactional
    @Override
    public GenreResponseDto update(long id, GenreRequestDto genreDto) {
        Genre genre = genreRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Genre with id %d to update not exist".formatted(id)));
        genre.setName(genreDto.name());
        return genreDtoMapper.convert(genre);
    }

    @Transactional
    @Override
    public GenreResponseDto insert(GenreRequestDto genreDto) {
        Genre genre = new Genre();
        genre.setName(genreDto.name());
        return genreDtoMapper.convert(genreRepository.save(genre));
    }
}
