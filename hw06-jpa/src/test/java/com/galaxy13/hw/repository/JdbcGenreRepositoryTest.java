package com.galaxy13.hw.repository;

import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.model.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("JDBC Genre Repository test")
@JdbcTest
@Import(JdbcGenreRepository.class)
class JdbcGenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    private List<Genre> dbGenres;

    @BeforeEach
    void setUp() {
        dbGenres = getDbGenres();
    }

    @DisplayName("Find all genres test")
    @Test
    void findAllGenresTest() {
        var allGenres = genreRepository.findAllGenres();
        assertThat(allGenres).containsExactlyElementsOf(dbGenres);
    }

    @DisplayName("Find genre by id test")
    @ParameterizedTest
    @MethodSource("getDbGenres")
    void findGenreByIdTest(Genre genre) {
        var genreById = genreRepository.findGenreById(genre.getId());
        assertThat(genreById).isPresent()
                .get()
                .isEqualTo(genre);
    }

    @DisplayName("Empty Optional on find of non-existing genre test")
    @Test
    void shouldReturnEmptyOptionalOnFindOfNonExistingGenreTest() {
        assertThat(genreRepository.findGenreById(-1)).isNotPresent();
        assertThat(genreRepository.findGenreById(0)).isNotPresent();
        assertThat(genreRepository.findGenreById(7)).isNotPresent();
    }

    @DisplayName("Inserting genre test")
    @Test
    void saveGenreTest() {
        var genreToSave = new Genre(0, "New Genre");
        var savedGenre = genreRepository.saveGenre(genreToSave);
        assertThat(savedGenre).isNotNull()
                .matches(genre -> genre.getId() > 0)
                .isEqualTo(genreToSave);

        var findGenreById = genreRepository.findGenreById(savedGenre.getId());
        assertThat(findGenreById).isPresent()
                .get()
                .isEqualTo(savedGenre);
    }

    @DisplayName("Updating genre test")
    @Test
    void updateGenreTest() {
        var genreToUpdate = new Genre(1, "New Genre");
        var updatedGenre = genreRepository.saveGenre(genreToUpdate);
        assertThat(updatedGenre).isNotNull()
                .isEqualTo(genreToUpdate);

        var findGenreById = genreRepository.findGenreById(updatedGenre.getId());
        assertThat(findGenreById).isPresent()
                .get()
                .isEqualTo(genreToUpdate);
    }

    @DisplayName("Throwing exception on updating of non-existing genre")
    @Test
    void shouldThrowExceptionOnUpdatingNonExistingGenre() {
        var genreToUpdate = new Genre(-1, "New Genre");
        assertThatThrownBy(() -> genreRepository.saveGenre(genreToUpdate)).isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("Find genres by ids")
    @ParameterizedTest
    @MethodSource("getDbGenreIds")
    void findGenresByIdsTest(Long genreNumber) {
        var genres = LongStream.range(1, genreNumber).boxed().collect(Collectors.toSet());
        var findGenres = genreRepository.findAllByIds(genres);
        assertThat(findGenres).isNotEmpty()
                .hasSize((int) (genreNumber - 1))
                .allMatch(genre -> genres.contains(genre.getId()));
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    private static List<Long> getDbGenreIds() {
        return LongStream.range(2, 8).boxed().toList();
    }
}
