package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.service.GenreDto;

import java.util.List;

public interface GenreService {
    List<GenreDto> findAllGenres();

    GenreDto findGenreById(long id);

    GenreDto update(long id, String genreName);

    GenreDto insert(GenreDto genreDto);
}
