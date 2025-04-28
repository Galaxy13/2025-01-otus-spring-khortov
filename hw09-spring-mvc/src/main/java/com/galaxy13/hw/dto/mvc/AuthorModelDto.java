package com.galaxy13.hw.dto.mvc;

import jakarta.validation.constraints.NotBlank;

public record AuthorModelDto(@NotBlank(message = "Author firstname is mandatory") String firstName, String lastName) {
}
