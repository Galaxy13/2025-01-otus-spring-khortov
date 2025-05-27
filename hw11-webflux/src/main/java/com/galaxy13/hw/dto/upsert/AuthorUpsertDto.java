package com.galaxy13.hw.dto.upsert;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthorUpsertDto(@NotNull(message = "Id field is mandatory") String id,
                              @NotBlank(message = "First name can't be empty") String firstName,
                              String lastName) {
}
