package com.galaxy13.hw.dto.upsert;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GenreUpsertDto(@NotNull(message = "Id field is mandatory") String id,
                             @NotBlank(message = "Genre name can't be empty") String name) {
}
