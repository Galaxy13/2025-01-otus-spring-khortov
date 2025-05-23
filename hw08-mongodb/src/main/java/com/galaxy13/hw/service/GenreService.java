package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.GenreDto;

import java.util.List;
import java.util.Optional;

public interface GenreService {
    List<GenreDto> findAllGenres();

    Optional<GenreDto> findGenreById(String id);

    GenreDto saveGenre(String id, String genreName);
}
