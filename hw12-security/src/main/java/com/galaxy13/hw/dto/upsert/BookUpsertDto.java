package com.galaxy13.hw.dto.upsert;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record BookUpsertDto(@NotNull(message = "Id field is mandatory") Long id,
                            @NotBlank(message = "Book title can't be empty") String title,
                            @NotNull(message = "Author id can't be null") Long authorId,
                            @NotNull(message = "Genre ids field is mandatory")
                            @NotEmpty(message = "Book must have at least one genre id") Set<Long> genreIds) {
}

