package com.galaxy13.hw.dto;

import com.galaxy13.hw.model.Author;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class AuthorDto {

    private final long id;

    private final String firstName;

    private final String lastName;

    public AuthorDto(Author author) {
        this.id = author.getId();
        this.firstName = author.getFirstName();
        this.lastName = author.getLastName();
    }
}
