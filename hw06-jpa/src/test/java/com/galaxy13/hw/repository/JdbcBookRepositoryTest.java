package com.galaxy13.hw.repository;

import com.galaxy13.hw.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import com.galaxy13.hw.model.Author;
import com.galaxy13.hw.model.Book;
import com.galaxy13.hw.model.Genre;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("JDBC Books Repository Test")
@JdbcTest
@Import({JdbcBookRepository.class, JdbcGenreRepository.class})
class JdbcBookRepositoryTest {

    @Autowired
    private JdbcBookRepository repositoryJdbc;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
    }

    @DisplayName("Should save book by id")
    @ParameterizedTest
    @MethodSource("getDbBooks")
    void shouldReturnCorrectBookById(Book expectedBook) {
        var actualBook = repositoryJdbc.findBookById(expectedBook.getId());
        assertThat(actualBook).isPresent()
                .get()
                .isEqualTo(expectedBook);
    }

    @DisplayName("Should find all books")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = repositoryJdbc.findAllBooks();
        var expectedBooks = dbBooks;

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
        actualBooks.forEach(System.out::println);
    }

    @DisplayName("Should save new book")
    @Test
    void shouldSaveNewBook() {
        var expectedBook = new Book(0, "BookTitle_10500", dbAuthors.getFirst(),
                List.of(dbGenres.get(0), dbGenres.get(2)));
        var returnedBook = repositoryJdbc.saveBook(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(repositoryJdbc.findBookById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @DisplayName("Should update book")
    @Test
    void shouldSaveUpdatedBook() {
        var expectedBook = new Book(1L, "BookTitle_10500", dbAuthors.get(2),
                List.of(dbGenres.get(4), dbGenres.get(5)));

        assertThat(repositoryJdbc.findBookById(expectedBook.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedBook);

        var returnedBook = repositoryJdbc.saveBook(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(repositoryJdbc.findBookById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @DisplayName("Should delete book by id")
    @Test
    void shouldDeleteBook() {
        assertThat(repositoryJdbc.findBookById(1L)).isPresent();
        repositoryJdbc.deleteBookById(1L);
        assertThat(repositoryJdbc.findBookById(1L)).isEmpty();
    }

    @DisplayName("Should return empty Optional on find non-existing book")
    @Test
    void shouldReturnEmptyOptionalOnFindNonExistingBook() {
        assertThat(repositoryJdbc.findBookById(-1L)).isNotPresent();
        assertThat(repositoryJdbc.findBookById(0)).isNotPresent();
        assertThat(repositoryJdbc.findBookById(4)).isNotPresent();
    }

    @DisplayName("Should throw exception on updating of non-existing book")
    @Test
    void shouldThrowExceptionOnUpdatingNonExistingBook() {
        var nonExistingBook = new Book(-1, "Title", dbAuthors.getFirst(), dbGenres.subList(0, 1));
        assertThatThrownBy(() -> repositoryJdbc.saveBook(nonExistingBook)).isInstanceOf(EntityNotFoundException.class);

        var nonExistingAuthor = new Book(1, "Title", new Author(-1, "name", "lastname"), dbGenres.subList(0, 1));
        assertThatThrownBy(() -> repositoryJdbc.saveBook(nonExistingAuthor)).isInstanceOf(RuntimeException.class);

        var nonExistingGenres = new Book(1, "Title", dbAuthors.getFirst(), List.of(new Genre(-1, "name")));
        assertThatThrownBy(() -> repositoryJdbc.saveBook(nonExistingGenres)).isInstanceOf(RuntimeException.class);
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id, "Surname_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(id,
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }

    private static List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }
}
