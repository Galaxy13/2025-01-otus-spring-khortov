package com.galaxy13.hw.dao;

import com.galaxy13.hw.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    List<Author> findAllAuthors();

    Optional<Author> findById(long id);

    Optional<Author> findByFullName(String firstName, String lastName);

    Author save(Author author);
}
