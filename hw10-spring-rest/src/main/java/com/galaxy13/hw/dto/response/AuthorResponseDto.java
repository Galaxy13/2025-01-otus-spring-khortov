package com.galaxy13.hw.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
@Getter
public class AuthorResponseDto {
    private final long id;

    private final String firstName;

    private final String lastName;
}
