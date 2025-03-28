package com.galaxy13.hw.dto;

import com.galaxy13.hw.model.Genre;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class GenreDto {
    private final long id;

    private final String name;

    public GenreDto(Genre genre){
        this.id = genre.getId();
        this.name = genre.getName();
    }
}
