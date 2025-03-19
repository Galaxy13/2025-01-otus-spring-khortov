package com.galaxy13.hw.dao;

import com.galaxy13.hw.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    List<Book> findAllBooks();

    Optional<Book> findBookById(long id);

    Book saveBook(Book book);

    void deleteBookById(long id);
}
