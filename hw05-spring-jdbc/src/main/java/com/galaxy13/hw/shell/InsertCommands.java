package com.galaxy13.hw.shell;

import com.galaxy13.hw.converter.BookConverter;
import com.galaxy13.hw.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.command.annotation.Command;

import java.util.Set;

@Command(command = "insert", group = "Insert commands")
@RequiredArgsConstructor
public class InsertCommands {
    private final BookService bookService;

    private final BookConverter bookConverter;

    @Command(description = "Insert book", command = "book")
    public String insertBook(String title, long authorId, Set<Long> genresIds) {
        var savedBook = bookService.insert(title, authorId, genresIds);
        return bookConverter.convertToString(savedBook);
    }
}
