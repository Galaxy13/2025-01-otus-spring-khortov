package com.galaxy13.hw.shell;

import com.galaxy13.hw.dto.AuthorDto;
import com.galaxy13.hw.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.command.annotation.Command;

import java.util.stream.Collectors;

@Command(group = "author", description = "Author Commands", command = "author")
@RequiredArgsConstructor
public class AuthorCommands {
    private final AuthorService authorService;

    @Command(command = "find all", alias = "aa", description = "Get info about all authors")
    public String findAllAuthors() {
        return authorService.findAllAuthors().stream()
                .map(AuthorDto::toString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @Command(command = "find", alias = "abid", description = "Find author info by id; params: {id: long}")
    public String findAuthorById(long id) {
        return authorService.findAuthorById(id)
                .map(AuthorDto::toString)
                .orElse("Author with id " + id + " not found");
    }

    @Command(command = "update", alias = "aupd",
            description = "Update author by id; params: {id: long} {firstname: String} {lastname:String}")
    public String updateAuthor(long id, String firstName, String lastName) {
        AuthorDto updatedAuthor = authorService.saveAuthor(id, firstName, lastName);
        return updatedAuthor.toString();
    }

    @Command(command = "save", alias = "ains",
            description = "Insert new author; params: {firstname: String} {lastname: String}")
    public String insertAuthor(String firstName, String lastName) {
        var savedAuthor = authorService.saveAuthor(0, firstName, lastName);
        return savedAuthor.toString();
    }
}
