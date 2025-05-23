package com.galaxy13.hw.repository;

import com.galaxy13.hw.model.Author;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("JPA Author Repository testing")
@DataJpaTest
class JpaAuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TestEntityManager em;

    private static Stream<Long> getDbAuthors() {
        return LongStream.range(1, 4).boxed();
    }

    private static Stream<Author> namedDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(num -> new Author(num, "Author_" + num, "Surname_" + num));
    }

    private static List<Author> insertDbAuthors() {
        List<Author> authors = new ArrayList<>();
        for (int i = 4; i < 7; i++) {
            authors.add(new Author(0, "Author_" + i, "Surname_" + i));
        }
        return authors;
    }

    @DisplayName("Find all authors test")
    @Test
    void shouldFindAllAuthors() {
        var actualAuthors = authorRepository.findAll();
        var expectedAuthors = LongStream.range(1, 4).boxed().map(id -> em.find(Author.class, id)).toList();
        assertThat(actualAuthors).isEqualTo(expectedAuthors);
    }

    @DisplayName("Find existing author by id")
    @ParameterizedTest
    @MethodSource("getDbAuthors")
    void shouldFindCorrectAuthorById(long authorId) {
        var expectedAuthor = em.find(Author.class, authorId);
        var actualAuthor = authorRepository.findById(expectedAuthor.getId());
        assertThat(actualAuthor).isPresent()
                .get()
                .isEqualTo(expectedAuthor);
    }

    @DisplayName("Empty Optional on find of non-existing author")
    @Test
    void shouldReturnEmptyOptionalOnFindNonExistingAuthor() {
        assertThat(authorRepository.findById(-1L)).isNotPresent();
        assertThat(authorRepository.findById(0L)).isNotPresent();
        assertThat(authorRepository.findById(4L)).isNotPresent();
    }

    @DisplayName("Insertion of authors")
    @ParameterizedTest
    @MethodSource("insertDbAuthors")
    void shouldInsertCorrectAuthor(Author expectedAuthor) {
        var actualAuthor = authorRepository.save(expectedAuthor);
        assertThat(actualAuthor).isNotNull()
                .matches(author1 -> author1.getId() > 0)
                .isEqualTo(expectedAuthor);

        var foundAuthor = em.find(Author.class, actualAuthor.getId());
        assertThat(foundAuthor).isNotNull()
                .isEqualTo(expectedAuthor);
    }

    @DisplayName("Update of author")
    @Test
    void shouldUpdateExistingAuthor() {
        var expectedAuthor = new Author(1, "New", "Author");
        var actualAuthor = authorRepository.save(expectedAuthor);
        assertThat(actualAuthor).isNotNull().usingRecursiveComparison().isEqualTo(expectedAuthor);

        var findAuthor = em.find(Author.class, actualAuthor.getId());
        assertThat(findAuthor).isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedAuthor);
    }

    @DisplayName("Exception on trying to update non-existing author")
    @Test
    void shouldThrowExceptionOnUpdateNonExistingAuthor() {
        var nonExistingAuthor = new Author(-1, "New", "Author");
        assertThatThrownBy(() -> authorRepository.save(nonExistingAuthor)).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("Should find author by first and last name")
    @ParameterizedTest
    @MethodSource("namedDbAuthors")
    void shouldFindCorrectAuthorByFirstAndLastName(Author expectedAuthor) {
        var firstName = expectedAuthor.getFirstName();
        var lastName = expectedAuthor.getLastName();
        var actualAuthor = authorRepository.findByFirstNameAndLastName(firstName, lastName);
        assertThat(actualAuthor).isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedAuthor);
    }

    @DisplayName("Should not find author by non-existing name")
    @Test
    void shouldNotFindAuthorByNonExistingName() {
        var wrongFirstName = "None";
        var wrongLastName = "None";
        assertThat(authorRepository.findByFirstNameAndLastName(wrongFirstName, wrongLastName)).isNotPresent();
    }
}
