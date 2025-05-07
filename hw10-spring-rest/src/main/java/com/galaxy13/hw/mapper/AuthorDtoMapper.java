package com.galaxy13.hw.mapper;

import com.galaxy13.hw.dto.AuthorDto;
import com.galaxy13.hw.model.Author;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AuthorDtoMapper implements Converter<Author, AuthorDto> {

    @Override
    public AuthorDto convert(Author source) {
        return new AuthorDto(source.getId(), source.getFirstName(), source.getLastName());
    }
}
