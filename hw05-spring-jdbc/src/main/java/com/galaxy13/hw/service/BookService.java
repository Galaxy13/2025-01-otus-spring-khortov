package com.galaxy13.hw.service;

import com.galaxy13.hw.model.Book;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookService {
    Optional<Book> findById(long id);

    List<Book> findAllBooks();

    Optional<Book> findByTitle(String title);

    List<Book> findByAuthor(long authorId);

    List<Book> findByGenres(Set<Long> genresIds);

    Book insert(String title, long authorId, Set<Long> genreIds);

    Book update(long id, String title, long authorId, Set<Long> genreIds);

    void deleteById(long id);
}
