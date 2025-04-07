package com.galaxy13.hw.converter;

import com.galaxy13.hw.dto.AuthorDto;
import com.galaxy13.hw.dto.BookDto;
import com.galaxy13.hw.dto.GenreDto;
import com.galaxy13.hw.model.Book;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;


@RequiredArgsConstructor
@Component
public class BookDtoConverter implements Converter<Book, BookDto> {

    private final AuthorDtoConverter authorDtoConverter;

    private final GenreDtoConverter genreDtoConverter;

    @Override
    public BookDto convert(@NonNull Book source) {
        AuthorDto authorDto = authorDtoConverter.convert(source.getAuthor());
        List<GenreDto> genres = source.getGenres().stream().map(genreDtoConverter::convert).toList();
        return new BookDto(source.getId(), source.getTitle(), authorDto, genres);
    }
}
