package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.BookDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookService {
    Optional<BookDto> findById(String id);

    List<BookDto> findAll();

    BookDto insert(String title, String authorId, Set<String> genreIds);

    BookDto update(String id, String title, String authorId, Set<String> genreIds);

    void deleteById(String id);
}
