package com.galaxy13.hw.dao;

import com.galaxy13.hw.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreRepository {
    List<Genre> findAllGenres();

    Genre findGenreById(long id);

    Genre findAllGenresByIds(Set<Long> ids);
}
