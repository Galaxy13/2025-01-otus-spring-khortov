package com.galaxy13.hw.converter;

import com.galaxy13.hw.dto.GenreDto;
import com.galaxy13.hw.model.Genre;
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GenreDtoConverter implements Converter<Genre, GenreDto> {

    @Override
    public GenreDto convert(@NonNull Genre source) {
        return new GenreDto(source.getId(), source.getName());
    }
}
