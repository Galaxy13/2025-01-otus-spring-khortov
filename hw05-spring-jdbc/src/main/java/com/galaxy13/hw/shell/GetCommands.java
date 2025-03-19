package com.galaxy13.hw.shell;

import com.galaxy13.hw.converter.BookConverter;
import com.galaxy13.hw.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.command.annotation.Command;

import java.util.stream.Collectors;

@Command(command = "get", group = "Get commands")
@RequiredArgsConstructor
public class GetCommands {
    private final BookService bookService;

    private final BookConverter bookConverter;

    @Command(description = "Find all books", command = "book all")
    public String findAllBooks() {
        return bookService.findAll().stream()
                .map(bookConverter::convertToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @Command(description = "Find book by id", command = "book by id")
    public String findBookById(long id) {
        return bookService.findById(id)
                .map(bookConverter::convertToString)
                .orElse("Book with id %d not found".formatted(id));
    }
}

