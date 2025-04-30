package com.galaxy13.hw.dto.request;

import jakarta.validation.constraints.NotBlank;

public record GenreRequestDto(@NotBlank(message = "Genre name field is mandatory") String name) {
}
