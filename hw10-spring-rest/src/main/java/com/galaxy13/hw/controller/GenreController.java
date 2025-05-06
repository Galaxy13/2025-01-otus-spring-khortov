package com.galaxy13.hw.controller;

import com.galaxy13.hw.dto.request.GenreRequestDto;
import com.galaxy13.hw.dto.response.GenreResponseDto;
import com.galaxy13.hw.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/")
    public List<GenreResponseDto> genres() {
        return genreService.findAllGenres()
                .stream()
                .sorted(Comparator.comparingLong(GenreResponseDto::getId))
                .toList();
    }

    @GetMapping("/{id}")
    public GenreResponseDto getGenre(@PathVariable long id) {
        return genreService.findGenreById(id);
    }

    @PutMapping("/{id}")
    public GenreResponseDto editGenre(@PathVariable("id") long id,
                                      @RequestBody GenreRequestDto genreDto) {
        return genreService.update(id, genreDto);
    }

    @PostMapping("/")
    public ResponseEntity<GenreResponseDto> saveGenre(@RequestBody GenreRequestDto genreDto) {
        return new ResponseEntity<>(genreService.insert(genreDto), HttpStatus.CREATED);
    }
}
