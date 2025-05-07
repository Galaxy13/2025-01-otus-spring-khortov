package com.galaxy13.hw.controller;

import com.galaxy13.hw.dto.upsert.AuthorUpsertDto;
import com.galaxy13.hw.dto.AuthorDto;
import com.galaxy13.hw.exception.MismatchedIdsException;
import com.galaxy13.hw.service.AuthorService;
import jakarta.validation.constraints.NotNull;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/author")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    public List<AuthorDto> authors() {
        return authorService.findAllAuthors();
    }

    @GetMapping("/{authorId}")
    public AuthorDto getAuthor(@PathVariable("authorId") long id) {
        return authorService.findAuthorById(id);
    }

    @PutMapping("/{authorId}")
    public AuthorDto editAuthor(@NotNull @PathVariable("authorId") long id,
                                @Validated @RequestBody AuthorUpsertDto authorDto) {
        if (authorDto.id() != id){
            throw new MismatchedIdsException(id, authorDto.id());
        }
        return authorService.update(authorDto);
    }

    @PostMapping
    public ResponseEntity<AuthorDto> newAuthor(@Validated @RequestBody AuthorUpsertDto authorDto) {
        return new ResponseEntity<>(authorService.create(authorDto), HttpStatus.CREATED);
    }
}
