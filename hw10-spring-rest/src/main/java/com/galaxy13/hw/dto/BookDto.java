package com.galaxy13.hw.dto;

import java.util.Set;

public record BookDto(long id,
                      String title,
                      AuthorDto author,
                      Set<GenreDto> genres) {
}
