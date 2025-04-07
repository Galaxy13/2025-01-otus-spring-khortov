package com.galaxy13.hw.converter;

import com.galaxy13.hw.dto.AuthorDto;
import com.galaxy13.hw.model.Author;
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AuthorDtoConverter implements Converter<Author, AuthorDto> {

    @Override
    public AuthorDto convert(@NonNull Author source) {
        return new AuthorDto(source.getId(), source.getFirstName(), source.getLastName());
    }
}
