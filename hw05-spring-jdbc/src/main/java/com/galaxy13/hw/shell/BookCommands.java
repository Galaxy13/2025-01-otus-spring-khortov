package com.galaxy13.hw.shell;

import com.galaxy13.hw.converter.BookConverter;
import com.galaxy13.hw.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.command.annotation.Command;

import java.util.Set;
import java.util.stream.Collectors;

@Command(command = "book", description = "Book commands", group = "Book")
@RequiredArgsConstructor
public class BookCommands {
    private final BookService bookService;

    private final BookConverter bookConverter;

    @Command(description = "Find all books", command = "find all", alias = "ab")
    public String findAllBooks() {
        return bookService.findAll().stream()
                .map(bookConverter::convertToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @Command(description = "Find book by id; params: {id: long}", command = "find", alias = "bbid")
    public String findBookById(long id) {
        return bookService.findById(id)
                .map(bookConverter::convertToString)
                .orElse("Book with id %d not found".formatted(id));
    }

    @Command(description = "Insert book; params: {bookTitle: String} {authorId: long} {genresIds: long...}",
            command = "save", alias = "bins")
    public String insertBook(String title, long authorId, Set<Long> genresIds) {
        var savedBook = bookService.insert(title, authorId, genresIds);
        return bookConverter.convertToString(savedBook);
    }

    @Command(description = "Update book; params: {id: long} {bookTitle: String} {authorId: long} {genresIds: long...}",
            command = "update", alias = "bupd")
    public String updateBook(long id, String title, long authorId, Set<Long> genresIds) {
        var savedBook = bookService.update(id, title, authorId, genresIds);
        return bookConverter.convertToString(savedBook);
    }

    @Command(description = "Delete book by id; params: {id: long}", command = "delete", alias = "bdel")
    public void deleteBook(long id) {
        bookService.deleteById(id);
    }
}
