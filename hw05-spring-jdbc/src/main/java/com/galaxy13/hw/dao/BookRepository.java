package com.galaxy13.hw.dao;

import com.galaxy13.hw.model.Author;
import com.galaxy13.hw.model.Book;
import com.galaxy13.hw.model.Genre;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    List<Book> findAllBooks();

    Optional<Book> findBookById(long id);

    Optional<Book> findBookByTitle(String title);

    List<Book> booksByAuthor(Author author);

    List<Book> booksByGenre(Genre title);

    Book addBook(Book book);

    void deleteBookById(long id);
}
