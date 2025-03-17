package com.galaxy13.hw.service;

import com.galaxy13.hw.dao.AuthorRepository;
import com.galaxy13.hw.dao.BookRepository;
import com.galaxy13.hw.dao.GenreRepository;
import com.galaxy13.hw.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    @Override
    public Optional<Book> findById(long id) {
        return Optional.empty();
    }

    @Override
    public List<Book> findAllBooks() {
        return List.of();
    }

    @Override
    public Optional<Book> findByTitle(String title) {
        return Optional.empty();
    }

    @Override
    public List<Book> findByAuthor(long authorId) {
        return List.of();
    }

    @Override
    public List<Book> findByGenres(Set<Long> genresIds) {
        return List.of();
    }

    @Override
    public Book insert(String title, long authorId, Set<Long> genreIds) {
        return null;
    }

    @Override
    public Book update(long id, String title, long authorId, Set<Long> genreIds) {
        return null;
    }

    @Override
    public void deleteById(long id) {

    }
}
