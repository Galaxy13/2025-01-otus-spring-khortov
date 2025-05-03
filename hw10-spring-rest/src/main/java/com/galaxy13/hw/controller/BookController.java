package com.galaxy13.hw.controller;

import com.galaxy13.hw.dto.response.BookResponseDto;
import com.galaxy13.hw.dto.request.BookRequestDto;
import com.galaxy13.hw.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;


    @GetMapping("/book")
    public List<BookResponseDto> getAllBooks() {
        return bookService.findAll();
    }

    @GetMapping("/book/{bookId}")
    public BookResponseDto getBook(@PathVariable("bookId") long id) {
        return bookService.findById(id);
    }

    @DeleteMapping("/book/{bookId}")
    public void deleteBook(@PathVariable("bookId") long id) {
        bookService.deleteById(id);
    }

    @PutMapping("/book/{bookId}")
    public BookResponseDto updateBook(@PathVariable("bookId") long id,
                                      @Validated @RequestBody BookRequestDto updateBook) {
        return bookService.update(id, updateBook);
    }

    @PostMapping("/book")
    public BookResponseDto newBook(@Validated @RequestBody BookRequestDto newBook) {
        return bookService.insert(newBook);
    }
}
