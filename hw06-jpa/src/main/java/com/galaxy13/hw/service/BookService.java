package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.BookDto;
import com.galaxy13.hw.model.Book;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookService {
    Optional<BookDto> findById(long id);

    List<BookDto> findAll();

    BookDto insert(String title, long authorId, Set<Long> genreIds);

    BookDto update(long id, String title, long authorId, Set<Long> genreIds);

    void deleteById(long id);
}
