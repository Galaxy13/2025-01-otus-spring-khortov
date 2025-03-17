package com.galaxy13.hw.dao;

import com.galaxy13.hw.model.Author;
import com.galaxy13.hw.model.Book;
import com.galaxy13.hw.model.Genre;

import java.util.List;

public interface BookRepository {
    List<Book> findAllBooks();

    Book findBookById(long id);

    Book findBookByTitle(String title);

    List<Book> booksByAuthor(Author author);

    List<Book> booksByGenre(Genre title);

    Book addBook(Book book);

    void deleteBookById(long id);
}
