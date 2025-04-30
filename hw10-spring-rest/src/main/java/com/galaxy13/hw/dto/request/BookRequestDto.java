package com.galaxy13.hw.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record BookRequestDto(@NotBlank(message = "Title of book is mandatory") String title,
                             @NotNull(message = "Author field is mandatory") Long authorId,
                             @NotNull(message = "Genre ids cannot be null")
                           @NotEmpty(message = "Genre ids cannot be empty") Set<Long> genreIds) {
}
