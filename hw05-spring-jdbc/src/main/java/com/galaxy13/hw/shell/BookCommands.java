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

    // book get all
    @Command(description = "Find all books", command = "find all")
    public String findAllBooks() {
        return bookService.findAll().stream()
                .map(bookConverter::convertToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    // book get {id}
    @Command(description = "Find book by id", command = "find")
    public String findBookById(long id) {
        return bookService.findById(id)
                .map(bookConverter::convertToString)
                .orElse("Book with id %d not found".formatted(id));
    }

    // book save {title} {authorId} {genreIds...}
    @Command(description = "Insert book", command = "save")
    public String insertBook(String title, long authorId, Set<Long> genresIds) {
        var savedBook = bookService.insert(title, authorId, genresIds);
        return bookConverter.convertToString(savedBook);
    }

    // book update {id} {title} {authorId} {genreIds...}
    @Command(description = "Update book", command = "update")
    public String updateBook(long id, String title, long authorId, Set<Long> genresIds) {
        var savedBook = bookService.update(id, title, authorId, genresIds);
        return bookConverter.convertToString(savedBook);
    }

    // book delete {id}
    @Command(description = "Delete book by id", command = "delete")
    public void deleteBook(long id) {
        bookService.deleteById(id);
    }
}
