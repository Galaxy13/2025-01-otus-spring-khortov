package com.galaxy13.hw.dto.upsert;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentUpsertDto(@NotNull(message = "Id field is mandatory") Long id,
                               @NotBlank(message = "Comment can't be empty") String text,
                               @NotNull(message = "Book id can't be null") Long bookId) {
}
