package com.galaxy13.hw.converter;

import com.galaxy13.hw.dto.AuthorDto;
import org.springframework.stereotype.Component;

@Component
public class AuthorDtoConverter implements Converter<AuthorDto> {
    @Override
    public String convertToString(AuthorDto author) {
        return "Id: %d, FullName: %s %s".formatted(author.getId(), author.getFirstName(), author.getLastName());
    }
}
