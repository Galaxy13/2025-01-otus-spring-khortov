package com.galaxy13.hw.repository;

import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.model.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("JDBC Author Repository testing")
@JdbcTest
@Import({JdbcAuthorRepository.class})
class JdbcAuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    private List<Author> dbAuthors;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
    }

    @DisplayName("Find all authors test")
    @Test
    void shouldFindAllAuthors() {
        var actualAuthors = authorRepository.findAllAuthors();
        assertThat(actualAuthors).containsExactlyElementsOf(dbAuthors);
    }

    @DisplayName("Find existing author by id")
    @ParameterizedTest
    @MethodSource("getDbAuthors")
    void shouldFindCorrectAuthorById(Author expectedAuthor) {
        var actualAuthor = authorRepository.findById(expectedAuthor.getId());
        assertThat(actualAuthor).isPresent()
                .get()
                .isEqualTo(expectedAuthor);
    }

    @DisplayName("Empty Optional on find of non-existing author")
    @Test
    void shouldReturnEmptyOptionalOnFindNonExistingAuthor() {
        assertThat(authorRepository.findById(-1L)).isNotPresent();
        assertThat(authorRepository.findById(0)).isNotPresent();
        assertThat(authorRepository.findById(4)).isNotPresent();
    }

    @DisplayName("Insertion of authors")
    @ParameterizedTest
    @MethodSource("insertDbAuthors")
    void shouldInsertCorrectAuthor(Author expectedAuthor) {
        var actualAuthor = authorRepository.save(expectedAuthor);
        assertThat(actualAuthor).isNotNull()
                .matches(author1 -> author1.getId() > 0)
                .isEqualTo(expectedAuthor);

        var findedAuthor = authorRepository.findById(actualAuthor.getId());
        assertThat(findedAuthor).isNotNull().isPresent()
                .get()
                .isEqualTo(expectedAuthor);
    }

    @DisplayName("Update of author")
    @Test
    void shouldUpdateExistingAuthor() {
        var expectedAuthor = new Author(1, "New", "Author");
        var actualAuthor = authorRepository.save(expectedAuthor);
        assertThat(actualAuthor).isNotNull().isEqualTo(expectedAuthor);

        var findAuthor = authorRepository.findById(actualAuthor.getId());
        assertThat(findAuthor).isPresent()
                .get()
                .isEqualTo(expectedAuthor);
    }

    @DisplayName("Exception on trying to update non-existing author")
    @Test
    void shouldThrowExceptionOnUpdateNonExistingAuthor() {
        var nonExistingAuthor = new Author(-1, "New", "Author");
        assertThatThrownBy(() -> authorRepository.save(nonExistingAuthor)).isInstanceOf(EntityNotFoundException.class);
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id, "Surname_" + id))
                .toList();
    }

    private static List<Author> insertDbAuthors() {
        List<Author> authors = new ArrayList<>();
        for (int i = 4; i < 7; i++) {
            authors.add(new Author(0, "Author_" + i, "Surname_" + i));
        }
        return authors;
    }
}
