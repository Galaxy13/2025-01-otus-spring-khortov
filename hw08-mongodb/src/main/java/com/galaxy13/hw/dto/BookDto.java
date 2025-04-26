package com.galaxy13.hw.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@RequiredArgsConstructor
@Getter
public class BookDto {
    private final String id;

    private final String title;

    private final AuthorDto author;

    private final List<GenreDto> genres;
}
