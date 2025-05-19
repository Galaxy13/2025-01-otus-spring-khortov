package com.galaxy13.hw.helper;

import com.galaxy13.hw.dto.AuthorDto;
import com.galaxy13.hw.dto.BookDto;
import com.galaxy13.hw.dto.CommentDto;
import com.galaxy13.hw.dto.GenreDto;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class TestData {
    private static List<BookDto> getBooks(List<AuthorDto> authors, List<GenreDto> genres) {
        return IntStream.range(1, 4).boxed().map(id -> new BookDto(
                id.toString(),
                "BookTitle_" + id,
                authors.get((id - 1)),
                new HashSet<>(genres.subList((id - 1) * 2, (id - 1) * 2 + 2))
        )).toList();
    }

    public static List<BookDto> getBooks() {
        return getBooks(getAuthors(), getGenres());
    }

    public static List<AuthorDto> getAuthors() {
        return LongStream.range(1, 4).boxed().map(id -> new AuthorDto(
                id.toString(), "Author_" + id, "Surname_" + id
        )).toList();
    }

    public static List<GenreDto> getGenres() {
        return LongStream.range(1, 7).boxed().map(id -> new GenreDto(
                id.toString(), "Genre_" + id
        )).toList();
    }

    public static List<CommentDto> getComments() {
        var book = new BookDto("1", null, null, null);
        var book2 = new BookDto("2", null, null, null);
        var book3 = new BookDto("3", null, null, null);
        return List.of(
                new CommentDto("1", "C_1", book.id()),
                new CommentDto("2", "C_2", book.id()),
                new CommentDto("3", "C_3", book2.id()),
                new CommentDto("4", "C_4", book2.id()),
                new CommentDto("5", "C_5", book3.id()),
                new CommentDto("6", "C_6", book3.id())
        );
    }

    public static Map<String, List<CommentDto>> bookIdToCommentMap() {
        return getComments().stream().collect(Collectors.groupingBy(
                CommentDto::bookId
        ));
    }
}
