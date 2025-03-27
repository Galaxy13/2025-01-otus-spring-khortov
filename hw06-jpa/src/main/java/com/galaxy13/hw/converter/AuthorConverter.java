package com.galaxy13.hw.converter;

import com.galaxy13.hw.model.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorConverter implements Converter<Author> {
    @Override
    public String convertToString(Author author) {
        return "Id: %d, FullName: %s %s".formatted(author.getId(), author.getFirstName(), author.getLastName());
    }
}
