package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.service.BookDto;
import com.galaxy13.hw.dto.mvc.BookModelDto;

import java.util.List;

public interface BookService {
    BookDto findById(long id);

    List<BookDto> findAll();

    BookDto insert(BookModelDto newBook);

    BookDto update(long bookId, BookModelDto updateBook);

    void deleteById(long id);
}
