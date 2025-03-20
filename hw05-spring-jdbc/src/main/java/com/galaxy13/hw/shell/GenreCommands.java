package com.galaxy13.hw.shell;

import com.galaxy13.hw.converter.GenreConverter;
import com.galaxy13.hw.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.command.annotation.Command;

import java.util.stream.Collectors;

@Command(group = "genre", description = "Genre Commands", command = "genre")
@RequiredArgsConstructor
public class GenreCommands {
    private final GenreService genreService;

    private final GenreConverter genreConverter;

    @Command(command = "find all", description = "Find all genres")
    public String findAllGenres() {
        return genreService.findAllGenres()
                .stream()
                .map(genreConverter::convertToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @Command(command = "find", description = "Find genre by id")
    public String findGenreById(long id) {
        return genreService.findGenreById(id)
                .map(genreConverter::convertToString)
                .orElse("Genre with id " + id + " not found");
    }

    @Command(command = "insert", description = "Insert new genre")
    public String insertGenre(String genreName) {
        var savedGenre = genreService.saveGenre(0, genreName);
        return genreConverter.convertToString(savedGenre);
    }
}
