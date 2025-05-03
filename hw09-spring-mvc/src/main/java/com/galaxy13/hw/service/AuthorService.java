package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.mvc.AuthorModelDto;
import com.galaxy13.hw.dto.service.AuthorDto;

import java.util.List;

public interface AuthorService {
    List<AuthorDto> findAllAuthors();

    AuthorDto findAuthorById(long id);

    AuthorDto insert(AuthorModelDto newAuthor);

    AuthorDto update(long id, AuthorModelDto updatedAuthor);
}
