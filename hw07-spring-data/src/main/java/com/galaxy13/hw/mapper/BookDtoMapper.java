package com.galaxy13.hw.mapper;

import com.galaxy13.hw.dto.AuthorDto;
import com.galaxy13.hw.dto.BookDto;
import com.galaxy13.hw.dto.GenreDto;
import com.galaxy13.hw.model.Book;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

import java.util.List;


@RequiredArgsConstructor
@Component
public class BookDtoMapper implements Converter<Book, BookDto> {

    private final AuthorDtoMapper authorDtoMapper;

    private final GenreDtoMapper genreDtoMapper;

    @Override
    public BookDto convert(@NonNull Book source) {
        AuthorDto authorDto = authorDtoMapper.convert(source.getAuthor());
        List<GenreDto> genres = source.getGenres().stream().map(genreDtoMapper::convert).toList();
        return new BookDto(source.getId(), source.getTitle(), authorDto, genres);
    }
}
