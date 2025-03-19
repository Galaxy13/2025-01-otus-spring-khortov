package com.galaxy13.hw.service;

import com.galaxy13.hw.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    List<Author> findAllAuthors();

    Optional<Author> findAuthorById(long id);
}
