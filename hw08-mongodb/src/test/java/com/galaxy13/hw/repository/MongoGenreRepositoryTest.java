package com.galaxy13.hw.repository;

import com.galaxy13.hw.AbstractBaseMongoTest;
import com.galaxy13.hw.model.Genre;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JPA Genre Repository test")
@DataMongoTest
class MongoGenreRepositoryTest extends AbstractBaseMongoTest {

    @Autowired
    private GenreRepository genreRepository;

    private static Stream<Long> getDbGenreIds() {
        return LongStream.range(2, 8).boxed();
    }

    @DisplayName("Find genres by ids")
    @ParameterizedTest
    @MethodSource("getDbGenreIds")
    void findGenresByIdsTest(Long genreNumber) {
        var genresIds = LongStream.range(1, genreNumber).boxed().map(Object::toString).collect(Collectors.toSet());
        var expectedGenres = genresIds.stream().map(id -> getMongoTemplate().findById(id, Genre.class)).toList();
        var actualGenres = genreRepository.findByIdIn(genresIds);
        assertThat(actualGenres).isNotEmpty()
                .hasSize((int) (genreNumber - 1))
                .usingRecursiveComparison()
                .isEqualTo(expectedGenres);
    }
}
