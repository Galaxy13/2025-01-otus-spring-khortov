package com.galaxy13.hw.dto.mvc;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record CommentModelDto(@NotBlank(message = "Comment can't be empty") String comment,
                              @NotNull(message = "Book id field is mandatory") long bookId) {
}
