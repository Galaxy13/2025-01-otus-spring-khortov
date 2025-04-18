package com.galaxy13.hw.service;

import com.galaxy13.hw.model.Book;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookService {
    Optional<Book> findById(long id);

    List<Book> findAll();

    Book insert(String title, long authorId, Set<Long> genreIds);

    Book update(long id, String title, long authorId, Set<Long> genreIds);

    void deleteById(long id);
}
