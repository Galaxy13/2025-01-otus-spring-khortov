package com.galaxy13.hw.converter;

import com.galaxy13.hw.dto.GenreDto;
import org.springframework.stereotype.Component;

@Component
public class GenreDtoConverter implements Converter<GenreDto> {
    @Override
    public String convertToString(GenreDto genre) {
        return "Id: %d, Name: %s".formatted(genre.getId(), genre.getName());
    }
}
