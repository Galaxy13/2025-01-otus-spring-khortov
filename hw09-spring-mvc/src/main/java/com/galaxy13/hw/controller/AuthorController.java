package com.galaxy13.hw.controller;

import com.galaxy13.hw.dto.AuthorDto;
import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.service.AuthorService;
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
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/authors")
    public String authors(Model model) {
        List<AuthorDto> authors = authorService.findAllAuthors()
                .stream()
                .sorted(Comparator.comparingLong(AuthorDto::getId))
                .toList();
        model.addAttribute("authors", authors);
        return "authors";
    }

    @GetMapping("/authors/edit")
    public String editAuthor(Model model, @RequestParam("id") long id) {
        AuthorDto authorDto = authorService.findAuthorById(id).orElseThrow(() ->
                new EntityNotFoundException("Author with id %d not found".formatted(id)));
        model.addAttribute("author", authorDto);
        return "author_edit";
    }

    @PostMapping("/authors/edit")
    public String editAuthor(@ModelAttribute("author") AuthorDto authorDto) {
        authorService.saveAuthor(authorDto.getId(), authorDto.getFirstName(), authorDto.getLastName());
        return "redirect:/authors";
    }

    @PostMapping("/authors/new")
    public String newAuthor(@RequestParam("firstName") String firstName,
                            @RequestParam("lastName") String lastName) {
        authorService.saveAuthor(0, firstName, lastName);
        return "redirect:/authors";
    }
}
