package com.galaxy13.hw.dto;

import com.galaxy13.hw.model.Book;
import lombok.Getter;

import java.util.List;

@Getter
public class BookDto {
    private final long id;

    private final String title;

    private final AuthorDto author;

    private final List<GenreDto> genres;

    public BookDto (Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = new AuthorDto(book.getAuthor());
        this.genres = book.getGenres().stream().map(GenreDto::new).toList();
    }
}
