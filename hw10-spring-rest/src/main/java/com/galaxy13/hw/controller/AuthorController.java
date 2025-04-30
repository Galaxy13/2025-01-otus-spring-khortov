package com.galaxy13.hw.controller;

import com.galaxy13.hw.dto.request.AuthorRequestDto;
import com.galaxy13.hw.dto.response.AuthorResponseDto;
import com.galaxy13.hw.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/author")
    public List<AuthorResponseDto> authors() {
        return authorService.findAllAuthors()
                .stream()
                .sorted(Comparator.comparingLong(AuthorResponseDto::getId))
                .toList();
    }

    @GetMapping("/author/{authorId}")
    public AuthorResponseDto getAuthor(@PathVariable("authorId") long id) {
        return authorService.findAuthorById(id);
    }

    @PutMapping("/author/{authorId}")
    public AuthorResponseDto editAuthor(@PathVariable("authorId") long id,
                                        @Validated @RequestBody AuthorRequestDto authorDto) {
        return authorService.update(id, authorDto);
    }

    @PostMapping("/author")
    public AuthorResponseDto newAuthor(@Validated @RequestBody AuthorRequestDto authorDto) {
        return authorService.insert(authorDto);
    }
}
