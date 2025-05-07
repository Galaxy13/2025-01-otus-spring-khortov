package com.galaxy13.hw.controller;

import com.galaxy13.hw.dto.GenreDto;
import com.galaxy13.hw.dto.upsert.GenreUpsertDto;
import com.galaxy13.hw.exception.controller.MismatchedIdsException;
import com.galaxy13.hw.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/v1/genre")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    public List<GenreDto> genres() {
        return genreService.findAllGenres()
                .stream()
                .sorted(Comparator.comparingLong(GenreDto::id))
                .toList();
    }

    @GetMapping("/{id}")
    public GenreDto getGenre(@PathVariable long id) {
        return genreService.findGenreById(id);
    }

    @PutMapping("/{id}")
    public GenreDto editGenre(@PathVariable("id") long id,
                              @Validated @RequestBody GenreUpsertDto genreDto) {
        if (id != genreDto.id()) {
            throw new MismatchedIdsException(id, genreDto.id());
        }
        return genreService.update(genreDto);
    }

    @PostMapping
    public ResponseEntity<GenreDto> saveGenre(@Validated @RequestBody GenreUpsertDto genreDto) {
        return new ResponseEntity<>(genreService.create(genreDto), HttpStatus.CREATED);
    }
}
