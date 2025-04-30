package com.galaxy13.hw.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentRequestDto(@NotBlank(message = "Comment text can't be empty") String comment,
                                @NotNull(message = "Book id field is mandatory") long bookId) {
}
