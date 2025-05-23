package com.galaxy13.hw.controller;

import com.galaxy13.hw.dto.service.AuthorDto;
import com.galaxy13.hw.dto.service.BookDto;
import com.galaxy13.hw.dto.mvc.BookModelDto;
import com.galaxy13.hw.dto.service.GenreDto;
import com.galaxy13.hw.service.AuthorService;
import com.galaxy13.hw.service.BookService;
import com.galaxy13.hw.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;

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

    @GetMapping("/books/{bookId}")
    public String editBook(@PathVariable("bookId") long id, Model model) {
        BookDto book = bookService.findById(id);
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

    @PostMapping("/books/{bookId}/delete")
    public String deleteBook(@PathVariable("bookId") long id) {
        bookService.deleteById(id);
        return REDIRECT;
    }

    @PostMapping("/books/{bookId}")
    public String updateBook(@PathVariable("bookId") long id,
                             @Validated @ModelAttribute("book") BookModelDto updateBook) {
        bookService.update(id, updateBook);
        return REDIRECT;
    }

    @PostMapping("/books")
    public String newBook(@Validated @ModelAttribute("book") BookModelDto newBook) {
        bookService.insert(newBook);
        return REDIRECT;
    }
}
