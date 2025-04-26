package com.galaxy13.hw.controller;

import com.galaxy13.hw.dto.AuthorDto;
import com.galaxy13.hw.dto.BookDto;
import com.galaxy13.hw.dto.GenreDto;
import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.service.AuthorService;
import com.galaxy13.hw.service.BookService;
import com.galaxy13.hw.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {

    private static final String REDIRECT = "redirect:/books";

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/books")
    public String bookPage(Model model) {
        List<BookDto> allBooks = bookService.findAll();
        model.addAttribute("books", allBooks);
        return "book_storage";
    }

    @GetMapping("/books/edit")
    public String editBook(@RequestParam("id") long id, Model model) {
        BookDto book = bookService.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Book with id %d not found".formatted(id)));
        List<AuthorDto> authors = authorService.findAllAuthors();
        List<GenreDto> genres = genreService.findAllGenres();
        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "book_edit";
    }

    @GetMapping("/books/new")
    public String newBook(Model model) {
        List<AuthorDto> authors = authorService.findAllAuthors();
        List<GenreDto> genres = genreService.findAllGenres();
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "book_new";
    }

    @PostMapping("/books/delete")
    public String deleteBook(@RequestParam("id") long id) {
        bookService.deleteById(id);
        return REDIRECT;
    }

    @PostMapping("/books/edit")
    public String updateBook(@RequestParam("id") long id,
                             @RequestParam("title") String title,
                             @RequestParam("authorId") long authorId,
                             @RequestParam("genreIds") List<Long> genreIds) {
        bookService.update(id, title, authorId, new HashSet<>(genreIds));
        return REDIRECT;
    }

    @PostMapping("/books/new")
    public String newBook(@RequestParam("title") String title,
                          @RequestParam("authorId") long authorId,
                          @RequestParam("genreIds") List<Long> genreIds) {
        bookService.insert(title, authorId, new HashSet<>(genreIds));
        return REDIRECT;
    }
}
