package com.galaxy13.hw.shell;

import com.galaxy13.hw.dto.BookDto;
import com.galaxy13.hw.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.command.annotation.Command;

import java.util.Set;
import java.util.stream.Collectors;

@Command(command = "book", description = "Book commands", group = "Book")
@RequiredArgsConstructor
public class BookCommands {
    private final BookService bookService;

    @Command(description = "Find all books", command = "find all", alias = "ab")
    public String findAllBooks() {
        return bookService.findAll().stream()
                .map(BookDto::toString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @Command(description = "Find book by id; params: {id: long}", command = "find", alias = "bbid")
    public String findBookById(String id) {
        return bookService.findById(id)
                .map(BookDto::toString)
                .orElse("Book with id %s not found".formatted(id));
    }

    @Command(description = "Insert book; params: {bookTitle: String} {authorId: long} {genresIds: long...}",
            command = "save", alias = "bins")
    public String insertBook(String title, String authorId, Set<String> genresIds) {
        var savedBook = bookService.insert(title, authorId, genresIds);
        return savedBook.toString();
    }

    @Command(description = "Update book; params: {id: long} {bookTitle: String} {authorId: long} {genresIds: long...}",
            command = "update", alias = "bupd")
    public String updateBook(String  id, String title, String authorId, Set<String> genresIds) {
        var savedBook = bookService.update(id, title, authorId, genresIds);
        return savedBook.toString();
    }

    @Command(description = "Delete book by id; params: {id: long}", command = "delete", alias = "bdel")
    public void deleteBook(String id) {
        bookService.deleteById(id);
    }
}
