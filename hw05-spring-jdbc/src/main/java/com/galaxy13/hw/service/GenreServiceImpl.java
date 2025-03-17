package com.galaxy13.hw.service;


import com.galaxy13.hw.dao.GenreRepository;
import com.galaxy13.hw.model.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    public List<Genre> findAllGenres() {
        return genreRepository.findAllGenres();
    }
}
