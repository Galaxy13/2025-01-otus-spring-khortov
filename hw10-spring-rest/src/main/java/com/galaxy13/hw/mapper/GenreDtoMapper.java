package com.galaxy13.hw.mapper;

import com.galaxy13.hw.dto.response.GenreResponseDto;
import com.galaxy13.hw.model.Genre;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GenreDtoMapper implements Converter<Genre, GenreResponseDto> {

    @Override
    public GenreResponseDto convert(Genre source) {
        return new GenreResponseDto(source.getId(), source.getName());
    }
}
