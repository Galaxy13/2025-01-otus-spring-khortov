package com.galaxy13.hw.dao;

import com.galaxy13.hw.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreRepository {
    List<Genre> findAllGenres();

    Optional<Genre> findGenreById(long id);

    List<Genre> findAllByIds(Set<Long> ids);

    Genre saveGenre(Genre genre);
}
