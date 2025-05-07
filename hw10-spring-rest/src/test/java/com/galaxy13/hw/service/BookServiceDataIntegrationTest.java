package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.GenreDto;
import com.galaxy13.hw.dto.upsert.BookUpsertDto;
import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.mapper.BookDtoMapper;
import com.galaxy13.hw.model.Author;
import com.galaxy13.hw.model.Book;
import com.galaxy13.hw.model.Genre;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
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
class BookServiceDataIntegrationTest {
    private final List<Author> authors = getAuthors();

    private final List<Genre> genres = getGenres();

    @Autowired
    private BookService bookService;

    @Autowired
    private BookDtoMapper mapper;

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
        var expectedBooks = getBooks().stream().map(mapper::convert).collect(Collectors.toList());
        var actualBooks = bookService.findAll();

        assertThat(actualBooks).usingRecursiveComparison().isEqualTo(expectedBooks);
    }

    @DisplayName("Should find book by id")
    @ParameterizedTest
    @MethodSource("getBooks")
    void shouldFindBookById(Book expectedBook) {
        var expectedDto = mapper.convert(expectedBook);
        var actualBook = bookService.findById(expectedBook.getId());
        assertThat(actualBook)
                .usingRecursiveComparison()
                .isEqualTo(expectedDto);
    }

    @DisplayName("Should not find non-existing book")
    @ParameterizedTest
    @ValueSource(longs = {-1, 0, 4})
    void shouldNotFindNonExistingBook(long id) {
        assertThatThrownBy(() -> bookService.findById(id))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("Should insert new book")
    @Test
    void shouldCreateNewBook() {
        var newBook = new BookUpsertDto(0L, "Blade Runner",
                authors.getFirst().getId(), Set.of(genres.getFirst().getId()));

        var actualBook = bookService.create(newBook);

        assertThat(actualBook).isNotNull()
                .matches(book -> book.id() > 0)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .ignoringFields("id")
                .isEqualTo(newBook);
    }

    @DisplayName("Should update existing book")
    @Test
    void shouldUpdateExistingBook() {
        var bookIdx = 1L;
        var updateBook = new BookUpsertDto(bookIdx, "Blade Runner",
                authors.getFirst().getId(), Set.of(genres.getFirst().getId()));
        var actualBook = bookService.update(updateBook);

        assertThat(actualBook)
                .isNotNull()
                .matches(book ->
                        book.id() == bookIdx &&
                        book.title().equals(updateBook.title()) &&
                        book.author().id().equals(updateBook.authorId()) &&
                        book.genres().stream()
                                .map(GenreDto::id)
                                .collect(Collectors.toSet())
                                .equals(updateBook.genreIds()));
    }

    @DisplayName("Should not update non-existing book")
    @Test
    void shouldNotUpdateNonExistingBook() {
        var bookIdx = -1L;
        var updateBook = new BookUpsertDto(bookIdx, "Blade Runner",
                authors.getFirst().getId(),
                Set.of(genres.getFirst().getId()));
        assertThatThrownBy(() -> bookService.update(updateBook)).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("Should not update book to non-existing author or genre")
    @Test
    void shouldNotUpdateNonExistingAuthorOrGenre() {

        var bookIdx = 1L;
        var nonExistingAuthorBook =
                new BookUpsertDto(bookIdx,
                        "Blade Runner", -1L, Set.of(genres.getFirst().getId()));
        assertThatThrownBy(() -> bookService
                .update(nonExistingAuthorBook)).isInstanceOf(RuntimeException.class);

        var nonExistingGenreBook =
                new BookUpsertDto(bookIdx, "Blade Runner",
                        authors.getFirst().getId(),
                        Set.of(-1L));
        assertThatThrownBy(() -> bookService.update(nonExistingGenreBook))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("Should delete book from database")
    @Test
    void shouldDeleteBookFromDatabase() {
        bookService.findById(1);
        bookService.deleteById(1);
        assertThatThrownBy(() -> bookService.findById(1)).isInstanceOf(EntityNotFoundException.class);
    }
}
