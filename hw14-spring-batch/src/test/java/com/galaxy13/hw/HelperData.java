package com.galaxy13.hw;

import com.galaxy13.hw.model.jpa.JpaAuthor;
import com.galaxy13.hw.model.jpa.JpaBook;
import com.galaxy13.hw.model.jpa.JpaComment;
import com.galaxy13.hw.model.jpa.JpaGenre;

import java.util.Arrays;
import java.util.List;

public class HelperData {

    private static final List<JpaAuthor> AUTHORS = List.of(
            new JpaAuthor(1L, "Author_1", "Surname_1"),
            new JpaAuthor(2L, "Author_1", "Surname_1"),
            new JpaAuthor(3L, "Author_2", "Surname_2")
    );

    private static final List<JpaGenre> GENRES = List.of(
            new JpaGenre(1L, "Genre_1"),
            new JpaGenre(2L, "Genre_2"),
            new JpaGenre(3L, "Genre_3"),
            new JpaGenre(4L, "Genre_4"),
            new JpaGenre(5L, "Genre_5"),
            new JpaGenre(6L, "Genre_6")
    );

    private static final List<JpaBook> BOOKS = List.of(
            new JpaBook(1L, "BookTitle_1", AUTHORS.getFirst(), Arrays.asList(GENRES.getFirst(), GENRES.get(1))),
            new JpaBook(2L, "BookTitle_1", AUTHORS.get(1), Arrays.asList(GENRES.get(2), GENRES.get(3))),
            new JpaBook(3L, "BookTitle_2", AUTHORS.get(2), Arrays.asList(GENRES.get(4), GENRES.get(5)))
    );

    private static final List<JpaComment> COMMENTS = List.of(
            new JpaComment(1L, "C_1", BOOKS.getFirst()),
            new JpaComment(2L, "C_2", BOOKS.getFirst()),
            new JpaComment(3L, "C_3", BOOKS.get(1)),
            new JpaComment(4L, "C_4", BOOKS.get(1)),
            new JpaComment(5L, "C_5", BOOKS.get(2)),
            new JpaComment(6L, "C_6", BOOKS.get(2))
    );

    public static List<JpaAuthor> getAuthors() {
        return AUTHORS;
    }

    public static List<JpaGenre> getGenres() {
        return GENRES;
    }

    public static List<JpaBook> getBooks() {
        return BOOKS;
    }

    public static List<JpaComment> getComments() {
        return COMMENTS;
    }
}