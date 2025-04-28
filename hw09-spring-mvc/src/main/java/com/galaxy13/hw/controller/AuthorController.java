package com.galaxy13.hw.controller;

import com.galaxy13.hw.dto.mvc.AuthorModelDto;
import com.galaxy13.hw.dto.service.AuthorDto;
import com.galaxy13.hw.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;

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

    @GetMapping("/authors/{authorId}")
    public String editAuthor(Model model, @PathVariable("authorId") long id) {
        AuthorDto authorDto = authorService.findAuthorById(id);
        model.addAttribute("author", authorDto);
        return "author_edit";
    }

    @PostMapping("/authors/{authorId}")
    public String editAuthor(@PathVariable("authorId") long id,
                             @Validated @ModelAttribute("author") AuthorModelDto authorDto) {
        authorService.update(id, authorDto);
        return "redirect:/authors";
    }

    @PostMapping("/authors")
    public String newAuthor(@Validated @ModelAttribute("author") AuthorModelDto authorDto) {
        authorService.insert(authorDto);
        return "redirect:/authors";
    }
}
