package com.galaxy13.hw.helper;

import com.galaxy13.hw.dto.service.AuthorDto;
import com.galaxy13.hw.dto.service.BookDto;
import com.galaxy13.hw.dto.service.CommentDto;
import com.galaxy13.hw.dto.service.GenreDto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class TestData {
    private static List<BookDto> getBooks(List<AuthorDto> authors, List<GenreDto> genres) {
        return IntStream.range(1, 4).boxed().map(id -> new BookDto(
                id,
                "BookTitle_" + id,
                authors.get((id - 1)),
                genres.subList((id - 1) * 2, (id - 1) * 2 + 2)
        )).toList();
    }

    public static List<BookDto> getBooks() {
        return getBooks(getAuthors(), getGenres());
    }

    public static List<AuthorDto> getAuthors() {
        return LongStream.range(1, 4).boxed().map(id -> new AuthorDto(
                id, "Author_" + id, "Surname_" + id
        )).toList();
    }

    public static List<GenreDto> getGenres() {
        return LongStream.range(1, 7).boxed().map(id -> new GenreDto(
                id, "Genre_" + id
        )).toList();
    }

    public static List<CommentDto> getComments() {
        var book = new BookDto(1, null, null, null);
        var book2 = new BookDto(2, null, null, null);
        var book3 = new BookDto(3, null, null, null);
        return List.of(
                new CommentDto(1L, "C_1", book.getId()),
                new CommentDto(2L, "C_2", book.getId()),
                new CommentDto(3L, "C_3", book2.getId()),
                new CommentDto(4L, "C_4", book2.getId()),
                new CommentDto(5L, "C_5", book3.getId()),
                new CommentDto(6L, "C_6", book3.getId())
        );
    }

    public static Map<Long, List<CommentDto>> bookIdToCommentMap() {
        return getComments().stream().collect(Collectors.groupingBy(
                CommentDto::getBookId
        ));
    }
}
