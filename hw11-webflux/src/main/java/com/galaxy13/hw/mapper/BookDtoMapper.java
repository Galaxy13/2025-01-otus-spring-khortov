package com.galaxy13.hw.mapper;

import com.galaxy13.hw.dto.AuthorDto;
import com.galaxy13.hw.dto.BookDto;
import com.galaxy13.hw.dto.GenreDto;
import com.galaxy13.hw.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BookDtoMapper implements Converter<Book, BookDto> {

    private final AuthorDtoMapper authorDtoMapper;

    private final GenreDtoMapper genreDtoMapper;

    @Override
    public BookDto convert(Book source) {
        AuthorDto authorDto = authorDtoMapper.convert(source.getAuthor());
        Set<GenreDto> genres = source.getGenres().stream().map(genreDtoMapper::convert).collect(Collectors.toSet());
        return new BookDto(source.getId(), source.getTitle(), authorDto, genres);
    }
}
