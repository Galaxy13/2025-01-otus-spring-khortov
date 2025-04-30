package com.galaxy13.hw.mapper;

import com.galaxy13.hw.dto.response.AuthorResponseDto;
import com.galaxy13.hw.model.Author;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AuthorDtoMapper implements Converter<Author, AuthorResponseDto> {

    @Override
    public AuthorResponseDto convert(Author source) {
        return new AuthorResponseDto(source.getId(), source.getFirstName(), source.getLastName());
    }
}
