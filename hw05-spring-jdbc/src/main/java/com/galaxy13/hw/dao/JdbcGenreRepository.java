package com.galaxy13.hw.dao;

import com.galaxy13.hw.model.Genre;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public class JdbcGenreRepository implements GenreRepository {
    @Override
    public List<Genre> findAllGenres() {
        return List.of();
    }

    @Override
    public Genre findGenreById(long id) {
        return null;
    }

    @Override
    public Genre findAllGenresByIds(Set<Long> ids) {
        return null;
    }
}
