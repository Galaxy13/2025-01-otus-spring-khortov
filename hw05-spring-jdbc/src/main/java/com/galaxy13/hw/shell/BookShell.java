package com.galaxy13.hw.shell;

import com.galaxy13.hw.converter.BookConverter;
import com.galaxy13.hw.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.command.annotation.Command;

import java.util.Set;
import java.util.stream.Collectors;

//@Command
@RequiredArgsConstructor
public class BookShell {
    private final BookService bookService;

    private final BookConverter bookConverter;

    @Command(description = "Find all books", command = "ab")
    public String findAllBooks() {
        return bookService.findAll().stream()
                .map(bookConverter::convertToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @Command(description = "Find book by id", command = "bbid")
    public String findBookById(long id) {
        return bookService.findById(id)
                .map(bookConverter::convertToString)
                .orElse("Book with id %d not found".formatted(id));
    }

    // bins newBook 1 1,6
    @Command(description = "Insert book", command = "bins")
    public String insertBook(String title, long authorId, Set<Long> genresIds) {
        var savedBook = bookService.insert(title, authorId, genresIds);
        return bookConverter.convertToString(savedBook);
    }

    // bupd 4 editedBook 3 2,5
    @Command(description = "Update book", command = "bupd")
    public String updateBook(long id, String title, long authorId, Set<Long> genresIds) {
        var savedBook = bookService.update(id, title, authorId, genresIds);
        return bookConverter.convertToString(savedBook);
    }

    // bdel 4
    @Command(description = "Delete book by id", command = "bdel")
    public void deleteBook(long id) {
        bookService.deleteById(id);
    }
}
