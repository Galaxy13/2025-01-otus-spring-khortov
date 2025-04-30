package com.galaxy13.hw.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AuthorRequestDto(@NotBlank(message = "Author firstname is mandatory") String firstName,
                               String lastName) {
}
