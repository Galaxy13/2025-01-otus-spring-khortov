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

    @Command(command = "find all", alias = "ag", description = "Find all genres")
    public String findAllGenres() {
        return genreService.findAllGenres()
                .stream()
                .map(genreConverter::convertToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @Command(command = "find", alias = "gbid", description = "Find genre by id; params: {id: long}")
    public String findGenreById(long id) {
        return genreService.findGenreById(id)
                .map(genreConverter::convertToString)
                .orElse("Genre with id " + id + " not found");
    }

    @Command(command = "save", alias = "gins", description = "Insert new genre; params: {genreName: String}")
    public String insertGenre(String genreName) {
        var savedGenre = genreService.saveGenre(0, genreName);
        return genreConverter.convertToString(savedGenre);
    }
}
