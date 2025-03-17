package com.galaxy13.hw.dao;

import com.galaxy13.hw.model.Author;
import com.galaxy13.hw.model.Book;
import com.galaxy13.hw.model.Genre;

import java.util.List;

public class JdbcBookRepository implements BookRepository {
    @Override
    public List<Book> findAllBooks() {
        return List.of();
    }

    @Override
    public Book findBookById(long id) {
        return null;
    }

    @Override
    public Book findBookByTitle(String title) {
        return null;
    }

    @Override
    public List<Book> booksByAuthor(Author author) {
        return List.of();
    }

    @Override
    public List<Book> booksByGenre(Genre title) {
        return List.of();
    }

    @Override
    public Book addBook(Book book) {
        return null;
    }

    @Override
    public void deleteBookById(long id) {

    }
}
