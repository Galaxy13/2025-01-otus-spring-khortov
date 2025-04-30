package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.request.AuthorRequestDto;
import com.galaxy13.hw.dto.response.AuthorResponseDto;

import java.util.List;

public interface AuthorService {
    List<AuthorResponseDto> findAllAuthors();

    AuthorResponseDto findAuthorById(long id);

    AuthorResponseDto insert(AuthorRequestDto newAuthor);

    AuthorResponseDto update(long id, AuthorRequestDto updatedAuthor);
}
