package com.galaxy13.hw.converter;

import com.galaxy13.hw.dto.AuthorDto;
import com.galaxy13.hw.dto.BookDto;
import com.galaxy13.hw.dto.GenreDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BookDtoConverter implements Converter<BookDto> {
    private final Converter<AuthorDto> authorConverter;

    private final Converter<GenreDto> genreConverter;

    @Override
    public String convertToString(BookDto book) {
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
