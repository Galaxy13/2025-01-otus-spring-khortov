package com.galaxy13.hw.controller;

import com.galaxy13.hw.dto.BookDto;
import com.galaxy13.hw.dto.upsert.BookUpsertDto;
import com.galaxy13.hw.exception.MismatchedIdsException;
import com.galaxy13.hw.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public List<BookDto> getAllBooks() {
        return bookService.findAll();
    }

    @GetMapping("/{bookId}")
    public BookDto getBook(@PathVariable("bookId") long id) {
        return bookService.findById(id);
    }

    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping("/{bookId}")
    public void deleteBook(@PathVariable("bookId") long id) {
        bookService.deleteById(id);
    }

    @PutMapping("/{bookId}")
    public BookDto updateBook(@PathVariable("bookId") long id,
                              @Validated @RequestBody BookUpsertDto bookDto) {
        if (id != bookDto.id()) {
            throw new MismatchedIdsException(id, bookDto.id());
        }
        return bookService.update(bookDto);
    }

    @PostMapping
    public ResponseEntity<BookDto> newBook(@Validated @RequestBody BookUpsertDto newBook) {
        return new ResponseEntity<>(bookService.create(newBook), HttpStatus.CREATED);
    }
}
