package com.galaxy13.hw.controller;

import com.galaxy13.hw.dto.service.GenreDto;
import com.galaxy13.hw.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/genres/{id}")
    public String editGenre(@PathVariable long id, Model model) {
        GenreDto genre = genreService.findGenreById(id);
        model.addAttribute("genre", genre);
        return "genre_edit";
    }

    @PostMapping("/genres/{id}")
    public String editGenre(@PathVariable("id") long id,
                            @RequestParam("name") String genreName) {
        genreService.update(id, genreName);
        return "redirect:/genres";
    }

    @PostMapping("/genres")
    public String saveGenre(@RequestParam("name") String name) {
        genreService.insert(new GenreDto(0, name));
        return "redirect:/genres";
    }
}
