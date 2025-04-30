package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.request.GenreRequestDto;
import com.galaxy13.hw.dto.response.GenreResponseDto;

import java.util.List;

public interface GenreService {
    List<GenreResponseDto> findAllGenres();

    GenreResponseDto findGenreById(long id);

    GenreResponseDto update(long id, GenreRequestDto genreDto);

    GenreResponseDto insert(GenreRequestDto genreDto);
}
