package com.galaxy13.hw.mapper;

import com.galaxy13.hw.dto.response.AuthorResponseDto;
import com.galaxy13.hw.dto.response.BookResponseDto;
import com.galaxy13.hw.dto.response.GenreResponseDto;
import com.galaxy13.hw.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

@RequiredArgsConstructor
@Component
public class BookDtoMapper implements Converter<Book, BookResponseDto> {

    private final AuthorDtoMapper authorDtoMapper;

    private final GenreDtoMapper genreDtoMapper;

    @Override
    public BookResponseDto convert(Book source) {
        AuthorResponseDto authorDto = authorDtoMapper.convert(source.getAuthor());
        List<GenreResponseDto> genres = source.getGenres().stream().map(genreDtoMapper::convert).toList();
        return new BookResponseDto(source.getId(), source.getTitle(), authorDto, genres);
    }
}
