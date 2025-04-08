package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.AuthorDto;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    List<AuthorDto> findAllAuthors();

    Optional<AuthorDto> findAuthorById(long id);

    AuthorDto saveAuthor(long id, String firstName, String lastName);
}
