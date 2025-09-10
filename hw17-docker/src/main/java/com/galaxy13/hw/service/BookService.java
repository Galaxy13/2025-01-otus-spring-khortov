package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.BookDto;
import com.galaxy13.hw.dto.upsert.BookUpsertDto;

import java.util.List;

public interface BookService {
    BookDto findById(long id);

    List<BookDto> findAll();

    BookDto create(BookUpsertDto newBook);

    BookDto update(BookUpsertDto updateBook);

    void deleteById(long id);
}
