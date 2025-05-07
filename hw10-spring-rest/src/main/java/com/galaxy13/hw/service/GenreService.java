package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.GenreDto;
import com.galaxy13.hw.dto.upsert.GenreUpsertDto;

import java.util.List;

public interface GenreService {
    List<GenreDto> findAllGenres();

    GenreDto findGenreById(long id);

    GenreDto update(GenreUpsertDto genreDto);

    GenreDto create(GenreUpsertDto genreDto);
}
