package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.response.BookResponseDto;
import com.galaxy13.hw.dto.request.BookRequestDto;

import java.util.List;

public interface BookService {
    BookResponseDto findById(long id);

    List<BookResponseDto> findAll();

    BookResponseDto insert(BookRequestDto newBook);

    BookResponseDto update(long bookId, BookRequestDto updateBook);

    void deleteById(long id);
}
