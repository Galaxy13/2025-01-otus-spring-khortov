package com.galaxy13.hw.dao;

import com.galaxy13.hw.model.Author;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcAuthorRepository implements AuthorRepository {
    @Override
    public List<Author> findAllAuthors() {
        return List.of();
    }

    @Override
    public Author findById(long id) {
        return null;
    }

    @Override
    public Author findByFullName(String firstName, String lastName) {
        return null;
    }
}
