package com.galaxy13.hw.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
@Getter
public class GenreDto {
    private final long id;

    private final String name;
}
