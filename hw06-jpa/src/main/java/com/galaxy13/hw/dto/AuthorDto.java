package com.galaxy13.hw.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
@Getter
public class AuthorDto {
    private final long id;

    private final String firstName;

    private final String lastName;
}
