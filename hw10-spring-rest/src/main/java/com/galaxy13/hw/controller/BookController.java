package com.galaxy13.hw.controller;

import com.galaxy13.hw.dto.response.BookResponseDto;
import com.galaxy13.hw.dto.request.BookRequestDto;
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

    @GetMapping("/")
    public List<BookResponseDto> getAllBooks() {
        return bookService.findAll();
    }

    @GetMapping("/{bookId}")
    public BookResponseDto getBook(@PathVariable("bookId") long id) {
        return bookService.findById(id);
    }

    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping("/{bookId}")
    public void deleteBook(@PathVariable("bookId") long id) {
        bookService.deleteById(id);
    }

    @PutMapping("/{bookId}")
    public BookResponseDto updateBook(@PathVariable("bookId") long id,
                                      @Validated @RequestBody BookRequestDto updateBook) {
        return bookService.update(id, updateBook);
    }

    @PostMapping("/")
    public ResponseEntity<BookResponseDto> newBook(@Validated @RequestBody BookRequestDto newBook) {
        return new ResponseEntity<>(bookService.insert(newBook), HttpStatus.CREATED);
    }
}
