package com.galaxy13.hw.mapper;

import com.galaxy13.hw.dto.service.GenreDto;
import com.galaxy13.hw.model.Genre;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GenreDtoMapper implements Converter<Genre, GenreDto> {

    @Override
    public GenreDto convert(Genre source) {
        return new GenreDto(source.getId(), source.getName());
    }
}
