package com.galaxy13.hw.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@RequiredArgsConstructor
@Getter
public class BookResponseDto {
    private final long id;

    private final String title;

    private final AuthorResponseDto author;

    private final List<GenreResponseDto> genres;
}
