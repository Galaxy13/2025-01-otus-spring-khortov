package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.AuthorDto;
import com.galaxy13.hw.dto.upsert.AuthorUpsertDto;

import java.util.List;

public interface AuthorService {
    List<AuthorDto> findAllAuthors();

    AuthorDto findAuthorById(long id);

    AuthorDto create(AuthorUpsertDto newAuthor);

    AuthorDto update(AuthorUpsertDto updatedAuthor);
}
