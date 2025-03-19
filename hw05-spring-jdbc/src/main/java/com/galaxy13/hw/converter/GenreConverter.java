package com.galaxy13.hw.converter;

import com.galaxy13.hw.model.Genre;
import org.springframework.stereotype.Component;

@Component
public class GenreConverter implements Converter<Genre> {
    @Override
    public String convertToString(Genre genre) {
        return "Id: %d, Name: %s".formatted(genre.getId(), genre.getName());
    }
}
