package com.galaxy13.hw.controller;

import com.galaxy13.hw.dto.GenreDto;
import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/genres")
    public String genres(Model model) {
        List<GenreDto> genres = genreService.findAllGenres()
                .stream()
                .sorted(Comparator.comparingLong(GenreDto::getId))
                .toList();
        model.addAttribute("genres", genres);
        return "genres";
    }

    @GetMapping("/genres/edit")
    public String editGenre(@RequestParam("id") long id, Model model) {
        GenreDto genre = genreService.findGenreById(id).orElseThrow(() ->
                new EntityNotFoundException("Genre with id %d not found".formatted(id)));
        model.addAttribute("genre", genre);
        return "genre_edit";
    }

    @PostMapping("/genres/edit")
    public String editGenre(@ModelAttribute("genre") GenreDto genre) {
        genreService.saveGenre(genre.getId(), genre.getName());
        return "redirect:/genres";
    }

    @PostMapping("/genres/new")
    public String saveGenre(@RequestParam("name") String name) {
        genreService.saveGenre(0, name);
        return "redirect:/genres";
    }
}
