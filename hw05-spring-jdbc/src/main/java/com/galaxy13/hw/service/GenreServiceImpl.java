package com.galaxy13.hw.service;


import com.galaxy13.hw.dao.GenreRepository;
import com.galaxy13.hw.model.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    public List<Genre> findAllGenres() {
        return genreRepository.findAllGenres();
    }

    @Override
    public Optional<Genre> findGenreById(long id) {
        return genreRepository.findGenreById(id);
    }

    @Override
    public Genre saveGenre(long id, String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Genre name cannot be null or empty");
        }

        Genre genre = new Genre(id, name);
        return genreRepository.saveGenre(genre);
    }
}
