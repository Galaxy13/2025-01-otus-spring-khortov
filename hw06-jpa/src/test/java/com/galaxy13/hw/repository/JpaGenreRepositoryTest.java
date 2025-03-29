package com.galaxy13.hw.repository;

import com.galaxy13.hw.model.Genre;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("JPA Genre Repository test")
@DataJpaTest
@Import(JpaGenreRepository.class)
class JpaGenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("Find all genres test")
    @Test
    void findAllGenresTest() {
        var allGenres = genreRepository.findAllGenres();
        var expectedGenres = LongStream.range(1, 7).boxed().map(id -> em.find(Genre.class, id)).toList();
        assertThat(allGenres).usingRecursiveComparison().isEqualTo(expectedGenres);
    }

    @DisplayName("Find genre by id test")
    @ParameterizedTest
    @MethodSource("getDbGenres")
    void findGenreByIdTest(long id) {
        var expectedGenre = em.find(Genre.class, id);
        var genreById = genreRepository.findGenreById(expectedGenre.getId());
        assertThat(genreById).isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedGenre);
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
                .usingRecursiveComparison()
                .isEqualTo(genreToUpdate);

        var findGenreById = genreRepository.findGenreById(updatedGenre.getId());
        assertThat(findGenreById).isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(genreToUpdate);
    }

    @DisplayName("Throwing exception on updating of non-existing genre")
    @Test
    void shouldThrowExceptionOnUpdatingNonExistingGenre() {
        var genreToUpdate = new Genre(-1, "New Genre");
        assertThatThrownBy(() -> genreRepository.saveGenre(genreToUpdate)).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("Find genres by ids")
    @ParameterizedTest
    @MethodSource("getDbGenreIds")
    void findGenresByIdsTest(Long genreNumber) {
        var genresIds = LongStream.range(1, genreNumber).boxed().collect(Collectors.toSet());
        var expectedGenres = genresIds.stream().map(id -> em.find(Genre.class, id)).toList();
        var actualGenres = genreRepository.findAllByIds(genresIds);
        assertThat(actualGenres).isNotEmpty()
                .hasSize((int) (genreNumber - 1))
                .usingRecursiveComparison()
                .isEqualTo(expectedGenres);
    }

    private static Stream<Long> getDbGenres() {
        return LongStream.range(1, 7).boxed();
    }

    private static Stream<Long> getDbGenreIds() {
        return LongStream.range(2, 8).boxed();
    }
}
