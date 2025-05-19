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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Integration Book service test")
@DataMongoTest
@Import({BookServiceImpl.class,})
@ComponentScan("com.galaxy13.hw.mapper")
class BookServiceIntegrationTest extends AbstractBaseMongoTest {
    private final List<Author> authors = getAuthors();

    private final List<Genre> genres = getGenres();

    @Autowired
    private BookService bookService;

    @Autowired
    private BookDtoMapper mapper;

    private static List<Book> getBooks(List<Author> authors, List<Genre> genres) {
        return IntStream.range(1, 4).boxed().map(id -> new Book(
                id.toString(),
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
                id.toString(), "Author_" + id, "Surname_" + id
        )).toList();
    }

    private static List<Genre> getGenres() {
        return LongStream.range(1, 7).boxed().map(id -> new Genre(
                id.toString(), "Genre_" + id
        )).toList();
    }

    @DisplayName("Should find all books")
    @Test
    void shouldFindAllBooks() {
        var expectedBooks = getBooks().stream().map(mapper::convert).collect(Collectors.toList());
        var actualBooks = bookService.findAll();

        StepVerifier.create(actualBooks)
                .expectNextSequence(expectedBooks)
                .verifyComplete();
    }

    @DisplayName("Should find book by id")
    @ParameterizedTest
    @MethodSource("getBooks")
    void shouldFindBookById(Book expectedBook) throws Exception {
        var actualBook = bookService.findById(expectedBook.getId());
        StepVerifier.create(actualBook)
                .expectNext(mapper.convert(expectedBook))
                .verifyComplete();
    }

    @DisplayName("Should not find non-existing book")
    @Test
    void shouldNotFindNonExistingBook() {
        var noBook = bookService.findById("-1");
        StepVerifier.create(noBook)
                .verifyError(EntityNotFoundException.class);
    }

    @DisplayName("Should insert new book")
    @Test
    void shouldInsertNewBook() {
        var expected = new BookUpsertDto("0", "Blade Runner",
                authors.getFirst().getId(), Set.of(genres.getFirst().getId()));

        var actualBook = bookService.create(expected);

        StepVerifier.create(actualBook)
                .assertNext(bookDto -> {
                    assertThat(bookDto.title()).isEqualTo(expected.title());
                    assertThat(bookDto.author().id()).isEqualTo(expected.authorId());
                    assertThat(bookDto.genres().stream().map(GenreDto::id).collect(Collectors.toSet()))
                            .isEqualTo(expected.genreIds());
                })
                .verifyComplete();
    }

    @DisplayName("Should update existing book")
    @Test
    void shouldUpdateExistingBook() {
        var bookIdx = "1";
        var updateBook = new BookUpsertDto(bookIdx, "Blade Runner",
                authors.getFirst().getId(), Set.of(genres.getFirst().getId()));
        var actualBook = bookService.update(updateBook);

        StepVerifier.create(actualBook)
                .assertNext(bookDto -> {
                    assertThat(bookDto.id()).isEqualTo(updateBook.id());
                    assertThat(bookDto.title()).isEqualTo(updateBook.title());
                    assertThat(bookDto.author().id()).isEqualTo(updateBook.authorId());
                    assertThat(bookDto.genres().stream().map(GenreDto::id).collect(Collectors.toSet()))
                            .isEqualTo(updateBook.genreIds());
                }).verifyComplete();
    }

    @DisplayName("Should not update non-existing book")
    @Test
    void shouldNotUpdateNonExistingBook() {
        var updateBook = new BookUpsertDto("-1", "Blade Runner",
                authors.getFirst().getId(),
                Set.of(genres.getFirst().getId()));
        var errUpdate = bookService.update(updateBook);
        StepVerifier.create(errUpdate)
                .expectError(EntityNotFoundException.class)
                .verify();
    }

    @DisplayName("Should not update book to non-existing author or genre")
    @Test
    void shouldNotUpdateNonExistingAuthorOrGenre() {
        Author author = authors.getFirst();
        author.setId("-1");
        Genre genre = genres.getFirst();
        genre.setId("-1");

        var nonExistingAuthorBook =
                new BookUpsertDto("1",
                        "Blade Runner", "-1", Set.of(genres.getFirst().getId()));
        var noAuthorErr = bookService.update(nonExistingAuthorBook);
        StepVerifier.create(noAuthorErr)
                .expectError(IllegalArgumentException.class)
                .verify();

        var nonExistingGenreBook = new BookUpsertDto("1",
                "Blade Runner", authors.getFirst().getId(), Set.of("-1", "2"));

        var noGenreErr = bookService.update(nonExistingGenreBook);
        StepVerifier.create(noGenreErr)
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @DisplayName("Should delete book from database")
    @Test
    void shouldDeleteBookFromDatabase() {
        var book = bookService.findById("1");
        StepVerifier.create(book)
                .assertNext(bookDto -> {
                    assertThat(bookDto).isNotNull();
                })
                .verifyComplete();

        var bookDeleted = bookService.deleteById("1");
        StepVerifier.create(bookDeleted)
                .verifyComplete();

        var noBook = bookService.findById("1");
        StepVerifier.create(noBook)
                .expectError(EntityNotFoundException.class)
                .verify();
    }
}
