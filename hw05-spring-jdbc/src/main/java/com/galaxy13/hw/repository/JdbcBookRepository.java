package com.galaxy13.hw.repository;

import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.model.Author;
import com.galaxy13.hw.model.Book;
import com.galaxy13.hw.model.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings({"java:S6203", "java:S1192", "java:S2259"})
@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    private final JdbcTemplate jdbcTemplate;

    private final GenreRepository genreRepository;

    @Override
    public List<Book> findAllBooks() {
        var booksWithAuthor = getAllBooksWithAuthors();
        var genresMap = getAllGenres();
        var bookGenreRelations = getAllBookGenreRelations();
        return mergeBookAndGenreInfo(booksWithAuthor, genresMap, bookGenreRelations);
    }

    @Override
    public Optional<Book> findBookById(long id) {
        Book book = getBookById(id);
        if (book != null) {
            List<Genre> genres = getBookGenres(id);
            for (Genre genre: genres) {
                book.addGenre(genre);
            }
        }
        return Optional.ofNullable(book);
    }

    @Override
    @Transactional
    public Book saveBook(Book book) {
        if (book.getId() == 0) {
            return insertBook(book);
        }
        return updateBook(book);
    }

    private Book insertBook(Book book) {
        var parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("title", book.getTitle())
                .addValue("authorId", book.getAuthor().getId());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedJdbcTemplate.update("""
                INSERT into BOOKS
                    (TITLE, AUTHOR_ID)
                VALUES ( :title, :authorId )""", parameterSource, keyHolder);
        book.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        batchInsertGenreRelations(book);
        return book;
    }

    private Book updateBook(Book book) {
        var parameterSource = new MapSqlParameterSource().addValue("id", book.getId())
                .addValue("title", book.getTitle())
                .addValue("authorId", book.getAuthor().getId());
        if (namedJdbcTemplate.update("""
                UPDATE BOOKS
                SET
                    TITLE = :title, AUTHOR_ID = :authorId
                WHERE ID = :id""", parameterSource) == 0) {
            throw new EntityNotFoundException("No book found with id: " + book.getId());
        }
        batchUpdateGenreRelations(book);
        return book;
    }

    @Override
    public void deleteBookById(long id) {
        Book deleteBook = new Book();
        deleteBook.setId(id);
        removeRelations(deleteBook);
        namedJdbcTemplate.update("""
               DELETE FROM
                   BOOKS
               WHERE ID = :id""", Map.of("id", deleteBook.getId()));
    }

    private void batchInsertGenreRelations(Book book) {
        namedJdbcTemplate.batchUpdate("""
                INSERT into GENRES_RELATIONSHIPS
                    (BOOK_ID, GENRE_ID)
                VALUES ( :bookId, :genreId )""", SqlParameterSourceUtils.createBatch(
                book.getGenres().stream().map(genre ->
                        new BookToGenreRelation(book.getId(), genre.getId())
                ).toList()));
    }

    private void batchUpdateGenreRelations(Book book) {
        removeRelations(book);
        batchInsertGenreRelations(book);
    }

    private void removeRelations(Book book) {
        namedJdbcTemplate.update("""
                DELETE FROM
                    GENRES_RELATIONSHIPS
                WHERE BOOK_ID = :id""", Map.of("id", book.getId()));
    }

    private record BookToGenreRelation(long bookId, long genreId) {
    }

    private Map<Long, Book> getAllBooksWithAuthors() {
        return jdbcTemplate.query("""
                SELECT
                    B.ID, TITLE, AUTHOR_ID, FIRSTNAME, LASTNAME
                FROM
                    BOOKS AS B
                INNER JOIN
                    AUTHORS A ON A.ID = B.AUTHOR_ID""", new BookResultSetExtractor());
    }

    private Map<Long, Genre> getAllGenres() {
        return genreRepository.findAllGenres().stream().collect(Collectors.toMap(Genre::getId, Function.identity()));
    }

    private List<BookToGenreRelation> getAllBookGenreRelations() {
        return jdbcTemplate.query("""
                SELECT
                    BOOK_ID, GENRE_ID
                FROM
                    GENRES_RELATIONSHIPS""", new BookGenreRelationMapper());
    }

    private List<Book> mergeBookAndGenreInfo(Map<Long, Book> books,
                                             Map<Long, Genre> genres,
                                             List<BookToGenreRelation> relations) {
        for (BookToGenreRelation relation: relations) {
            Book book = books.get(relation.bookId());
            Genre genre = genres.get(relation.genreId());
            book.addGenre(genre);
        }
        return books.values().stream().toList();
    }

    private Book getBookById(long id) {
        Map<Long, Book> book =  namedJdbcTemplate.query("""
                SELECT
                    B.ID, TITLE, AUTHOR_ID, FIRSTNAME, LASTNAME
                FROM
                    BOOKS AS B
                INNER JOIN
                    AUTHORS A on A.ID = B.AUTHOR_ID
                WHERE
                    B.ID = :id""",
                Map.of("id", id), new BookResultSetExtractor());
        return book.get(id);
    }

    private List<Genre> getBookGenres(long id) {
        List<BookToGenreRelation> relations = namedJdbcTemplate.query("""
                SELECT
                    BOOK_ID, GENRE_ID
                FROM
                    GENRES_RELATIONSHIPS
                WHERE
                    BOOK_ID = :id""",
                Map.of("id", id), new BookGenreRelationMapper());
        return genreRepository.findAllByIds(relations.stream()
                .map(BookToGenreRelation::genreId)
                .collect(Collectors.toSet()));
    }

    private static class BookGenreRelationMapper implements RowMapper<BookToGenreRelation> {
        @Override
        public BookToGenreRelation mapRow(ResultSet rs, int rowNum) throws SQLException {
            long bookId = rs.getLong("book_id");
            long genreId = rs.getLong("genre_id");
            return new BookToGenreRelation(bookId, genreId);
        }
    }

    private static class BookResultSetExtractor implements ResultSetExtractor<Map<Long, Book>> {

        @Override
        public Map<Long, Book> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, Book> books = new HashMap<>();
            while (rs.next()) {
                long id = rs.getLong("id");
                String title = rs.getString("title");
                Author author = getAuthor(rs);
                books.put(id, new Book(id, title, author, new ArrayList<>()));
            }
            return books;
        }

        private Author getAuthor(ResultSet rs) throws SQLException {
            long authorId = rs.getLong("author_id");
            String firstName = rs.getString("firstName");
            String lastName = rs.getString("lastName");
            return new Author(authorId, firstName, lastName);
        }
    }
}