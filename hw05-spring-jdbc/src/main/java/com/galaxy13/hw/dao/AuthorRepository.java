package com.galaxy13.hw.dao;

import com.galaxy13.hw.model.Author;

import java.util.List;

public interface AuthorRepository {
    List<Author> findAllAuthors();

    Author findById(long id);

    Author findByFullName(String firstName, String lastName);
}
