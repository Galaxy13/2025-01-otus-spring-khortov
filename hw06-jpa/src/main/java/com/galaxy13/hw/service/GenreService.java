package com.galaxy13.hw.service;

import com.galaxy13.hw.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {
    List<Genre> findAllGenres();

    Optional<Genre> findGenreById(long id);

    Genre saveGenre(long id, String genreName);
}
