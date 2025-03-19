package com.galaxy13.hw.converter;

import com.galaxy13.hw.model.Author;
import com.galaxy13.hw.model.Book;
import com.galaxy13.hw.model.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BookConverter implements Converter<Book> {
    private final Converter<Author> authorConverter;

    private final Converter<Genre> genreConverter;

    @Override
    public String convertToString(Book book) {
        var genresString = book.getGenres().stream()
                .map(genreConverter::convertToString)
                .map("{%s}"::formatted)
                .collect(Collectors.joining(", "));
        return "Id: %d, title: %s, author: {%s}, genres: [%s]".formatted(
                book.getId(),
                book.getTitle(),
                authorConverter.convertToString(book.getAuthor()),
                genresString);
    }
}
