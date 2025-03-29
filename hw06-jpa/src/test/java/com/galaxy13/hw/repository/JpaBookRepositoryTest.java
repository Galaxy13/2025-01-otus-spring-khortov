package com.galaxy13.hw.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import com.galaxy13.hw.model.Author;
import com.galaxy13.hw.model.Book;
import com.galaxy13.hw.model.Genre;

import java.util.List;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("JPA Books Repository Test")
@DataJpaTest
@Import({JpaBookRepository.class})
class JpaBookRepositoryTest {

    @Autowired
    private BookRepository repositoryJpa;

    @Autowired
    private EntityManager em;

    @DisplayName("Should save book by id")
    @ParameterizedTest
    @MethodSource("getDbBookIds")
    void shouldReturnCorrectBookById(long bookId) {
        var expectedBook = em.find(Book.class, bookId);
        var actualBook = repositoryJpa.findBookById(expectedBook.getId());
        assertThat(actualBook).isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);
    }

    @DisplayName("Should find all books")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = repositoryJpa.findAllBooks();
        var expectedBooks = Stream.of(1L, 2L, 3L).map(id -> em.find(Book.class, id)).toList();

        assertThat(actualBooks).usingRecursiveComparison().isEqualTo(expectedBooks);
    }

    @DisplayName("Should save new book")
    @Test
    void shouldSaveNewBook() {
        Author author = em.find(Author.class, 1);
        List<Genre> genres = List.of(em.find(Genre.class, 1), em.find(Genre.class, 2));
        var expectedBook = new Book(0, "BookTitle_10500", author,
                genres);
        var returnedBook = repositoryJpa.saveBook(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(repositoryJpa.findBookById(returnedBook.getId()))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(returnedBook);
    }

    @DisplayName("Should update book")
    @Test
    void shouldSaveUpdatedBook() {
        Author author = em.find(Author.class, 3);
        List<Genre> genres = List.of(em.find(Genre.class, 5), em.find(Genre.class, 6));
        var expectedBook = new Book(1L, "BookTitle_10500", author,
               genres);

        assertThat(repositoryJpa.findBookById(expectedBook.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedBook);

        var returnedBook = repositoryJpa.saveBook(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(repositoryJpa.findBookById(returnedBook.getId()))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(returnedBook);
    }

    @DisplayName("Should delete book by id")
    @Test
    void shouldDeleteBook() {
        assertThat(repositoryJpa.findBookById(3L)).isPresent();
        repositoryJpa.deleteBookById(3L);
        assertThat(repositoryJpa.findBookById(3L)).isEmpty();
    }

    @DisplayName("Should return empty Optional on find non-existing book")
    @Test
    void shouldReturnEmptyOptionalOnFindNonExistingBook() {
        assertThat(repositoryJpa.findBookById(-1L)).isNotPresent();
        assertThat(repositoryJpa.findBookById(0)).isNotPresent();
        assertThat(repositoryJpa.findBookById(4)).isNotPresent();
    }

    @DisplayName("Should throw exception on updating of non-existing book")
    @Test
    void shouldThrowExceptionOnUpdatingNonExistingBook() {
        var nonExistingBook = new Book(-1, "Title", em.find(Author.class, 1), List.of(em.find(Genre.class, 1)));
        assertThatThrownBy(() -> repositoryJpa.saveBook(nonExistingBook)).isInstanceOf(RuntimeException.class);
    }

    private static Stream<Long> getDbBookIds() {
        return LongStream.range(1, 4).boxed();
    }
}
