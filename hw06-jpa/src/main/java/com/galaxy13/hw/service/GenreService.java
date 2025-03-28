package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.GenreDto;
import com.galaxy13.hw.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {
    List<GenreDto> findAllGenres();

    Optional<GenreDto> findGenreById(long id);

    GenreDto saveGenre(long id, String genreName);
}
