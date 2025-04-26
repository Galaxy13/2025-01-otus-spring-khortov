package com.galaxy13.hw.repository;

import com.galaxy13.hw.AbstractBaseMongoTest;
import com.galaxy13.hw.model.Author;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Mongo Author Repository testing")
@DataMongoTest
class MongoAuthorRepositoryTest extends AbstractBaseMongoTest {

    @Autowired
    private AuthorRepository authorRepository;

    private static Stream<Author> namedDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(num -> new Author(num.toString(), "Author_" + num, "Surname_" + num));
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
