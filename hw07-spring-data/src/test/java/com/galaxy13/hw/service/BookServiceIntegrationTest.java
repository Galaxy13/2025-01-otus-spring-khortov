package com.galaxy13.hw.service;

import com.galaxy13.hw.model.Author;
import com.galaxy13.hw.model.Book;
import com.galaxy13.hw.model.Genre;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("java:S5778")
@DisplayName("Integration Book service test")
@DataJpaTest
@Import({BookServiceImpl.class})
@Transactional(propagation = Propagation.NEVER)
@Sql(scripts = {"/cleanup.sql", "/data.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ComponentScan("com.galaxy13.hw.mapper")
class BookServiceIntegrationTest {
    private final List<Author> authors = getAuthors();

    private final List<Genre> genres = getGenres();

    @Autowired
    private BookService bookService;

    private static List<Book> getBooks(List<Author> authors, List<Genre> genres) {
        return IntStream.range(1, 4).boxed().map(id -> new Book(
                id,
                "BookTitle_" + id,
                authors.get((id - 1)),
                genres.subList((id - 1) * 2, (id - 1) * 2 + 2)
        )).toList();
    }

    private static List<Book> getBooks() {
        return getBooks(getAuthors(), getGenres());
    }

    private static List<Author> getAuthors() {
        return LongStream.range(1, 4).boxed().map(id -> new Author(
                id, "Author_" + id, "Surname_" + id
        )).toList();
    }

    private static List<Genre> getGenres() {
        return LongStream.range(1, 7).boxed().map(id -> new Genre(
                id, "Genre_" + id
        )).toList();
    }

    @DisplayName("Should find all books")
    @Test
    void shouldFindAllBooks() {
        var expectedBooks = getBooks();
        var actualBooks = bookService.findAll();

        assertThat(actualBooks).usingRecursiveComparison().isEqualTo(expectedBooks);
    }

    @DisplayName("Should find book by id")
    @ParameterizedTest
    @MethodSource("getBooks")
    void shouldFindBookById(Book expectedBook) {
        var actualBook = bookService.findById(expectedBook.getId());
        assertThat(actualBook).isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);
    }

    @DisplayName("Should not find non-existing book")
    @Test
    void shouldNotFindNonExistingBook() {
        assertThat(bookService.findById(-1)).isNotPresent();
        assertThat(bookService.findById(0)).isNotPresent();
        assertThat(bookService.findById(4)).isNotPresent();
    }

    @DisplayName("Should insert new book")
    @Test
    void shouldInsertNewBook() {
        var newBook = new Book(0, "Blade Runner", authors.getFirst(), List.of(genres.getFirst()));
        var actualBook = bookService.insert(newBook.getTitle(),
                newBook.getAuthor().getId(),
                newBook.getGenres().stream().map(Genre::getId).collect(Collectors.toSet()));

        assertThat(actualBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .ignoringFields("id")
                .isEqualTo(newBook);
    }

    @DisplayName("Should update existing book")
    @Test
    void shouldUpdateExistingBook() {
        var updateBook = new Book(1, "Blade Runner", authors.getFirst(), List.of(genres.getFirst()));
        var actualBook = bookService.update(updateBook.getId(),
                updateBook.getTitle(),
                updateBook.getAuthor().getId(),
                updateBook.getGenres().stream().map(Genre::getId).collect(Collectors.toSet()));

        assertThat(actualBook).isNotNull().usingRecursiveComparison().isEqualTo(updateBook);
    }

    @DisplayName("Should not update non-existing book")
    @Test
    void shouldNotUpdateNonExistingBook() {
        var updateBook = new Book(-1, "Blade Runner", authors.getFirst(), List.of(genres.getFirst()));
        assertThatThrownBy(() -> bookService.update(updateBook.getId(),
                updateBook.getTitle(),
                updateBook.getAuthor().getId(),
                updateBook.getGenres().stream().map(Genre::getId).collect(Collectors.toSet()))
        ).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("Should not update book to non-existing author or genre")
    @Test
    void shouldNotUpdateNonExistingAuthorOrGenre() {
        Author author = authors.getFirst();
        author.setId(-1);
        Genre genre = genres.getFirst();
        genre.setId(-1);

        var nonExistingAuthorBook = new Book(1, "Blade Runner", author, List.of(genres.getFirst()));
        assertThatThrownBy(() -> bookService.update(
                nonExistingAuthorBook.getId(),
                nonExistingAuthorBook.getTitle(),
                nonExistingAuthorBook.getAuthor().getId(),
                nonExistingAuthorBook.getGenres().stream().map(Genre::getId).collect(Collectors.toSet())
        )).isInstanceOf(RuntimeException.class);

        var nonExistingGenreBook = new Book(-1, "Blade Runner", authors.getFirst(), List.of(genre));
        assertThatThrownBy(() -> bookService.update(
                nonExistingGenreBook.getId(),
                nonExistingGenreBook.getTitle(),
                nonExistingGenreBook.getAuthor().getId(),
                nonExistingGenreBook.getGenres().stream().map(Genre::getId).collect(Collectors.toSet())
        )).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("Should delete book from database")
    @Test
    void shouldDeleteBookFromDatabase() {
        assertThat(bookService.findById(1)).isPresent();
        bookService.deleteById(1);
        assertThat(bookService.findById(1)).isEmpty();
    }
}
