package com.galaxy13.hw.helper;

import com.galaxy13.hw.dto.response.AuthorResponseDto;
import com.galaxy13.hw.dto.response.BookResponseDto;
import com.galaxy13.hw.dto.response.CommentResponseDto;
import com.galaxy13.hw.dto.response.GenreResponseDto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class TestData {
    private static List<BookResponseDto> getBooks(List<AuthorResponseDto> authors, List<GenreResponseDto> genres) {
        return IntStream.range(1, 4).boxed().map(id -> new BookResponseDto(
                id,
                "BookTitle_" + id,
                authors.get((id - 1)),
                genres.subList((id - 1) * 2, (id - 1) * 2 + 2)
        )).toList();
    }

    public static List<BookResponseDto> getBooks() {
        return getBooks(getAuthors(), getGenres());
    }

    public static List<AuthorResponseDto> getAuthors() {
        return LongStream.range(1, 4).boxed().map(id -> new AuthorResponseDto(
                id, "Author_" + id, "Surname_" + id
        )).toList();
    }

    public static List<GenreResponseDto> getGenres() {
        return LongStream.range(1, 7).boxed().map(id -> new GenreResponseDto(
                id, "Genre_" + id
        )).toList();
    }

    public static List<CommentResponseDto> getComments() {
        var book = new BookResponseDto(1, null, null, null);
        var book2 = new BookResponseDto(2, null, null, null);
        var book3 = new BookResponseDto(3, null, null, null);
        return List.of(
                new CommentResponseDto(1L, "C_1", book.getId()),
                new CommentResponseDto(2L, "C_2", book.getId()),
                new CommentResponseDto(3L, "C_3", book2.getId()),
                new CommentResponseDto(4L, "C_4", book2.getId()),
                new CommentResponseDto(5L, "C_5", book3.getId()),
                new CommentResponseDto(6L, "C_6", book3.getId())
        );
    }

    public static Map<Long, List<CommentResponseDto>> bookIdToCommentMap() {
        return getComments().stream().collect(Collectors.groupingBy(
                CommentResponseDto::getBookId
        ));
    }
}
