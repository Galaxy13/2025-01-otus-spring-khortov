package com.galaxy13.hw.dao;

import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.model.Author;
import com.galaxy13.hw.model.Book;
import com.galaxy13.hw.model.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
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
import java.util.*;
import java.util.function.Supplier;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Book> findAllBooks() {
        return jdbcTemplate.query(
                """     
                        SELECT
                            b.id,
                            b.title,
                            b.author_id,
                            a.firstName,
                            a.lastName,
                            ARRAY_AGG(g2.id) AS genre_ids,
                            ARRAY_AGG(g2.genre_title) AS genre_titles
                        FROM
                            books AS b
                        JOIN
                            genres_relationships AS gR ON b.id = gR.book_id
                        JOIN
                            genres AS g2 ON gR.genre_id = g2.id
                        JOIN
                            authors AS a ON b.author_id = a.id
                        GROUP BY
                            b.id
                        ORDER BY
                            b.id""", new BookRowMapper());
    }

    @Override
    public Optional<Book> findBookById(long id) {
        return Optional.ofNullable(getNullableResult(() -> namedJdbcTemplate.queryForObject(
                """
                        SELECT
                            b.id,
                            b.title,
                            b.author_id,
                            a.firstName,
                            a.lastName,
                            ARRAY_AGG(g2.id) AS genre_ids,
                            ARRAY_AGG(g2.genre_title) AS genre_titles
                        FROM
                            books AS b
                                JOIN
                            genres_relationships AS gR ON b.id = gR.book_id
                                JOIN
                            genres AS g2 ON gR.genre_id = g2.id
                                JOIN
                            authors AS a ON b.author_id = a.id
                        WHERE
                            b.id = :id
                        GROUP BY
                            b.id
                        ORDER BY
                            b.id
                        """,
                Map.of("id", id), new BookRowMapper())));
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
        return book;
    }

    @Override
    public void deleteBookById(long id) {

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

    private <T> T getNullableResult(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    private record BookToGenreRelation(long bookId, long genreId) {
    }

    private static class BookRowMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String title = rs.getString("title");
            Author author = getAuthor(rs);
            Book book = new Book(id, title, author, new ArrayList<>());
            addGenresToBook(rs, book);
            return book;
        }

        private Author getAuthor(ResultSet rs) throws SQLException {
            long authorId = rs.getLong("author_id");
            String firstName = rs.getString("firstName");
            String lastName = rs.getString("lastName");
            return new Author(authorId, firstName, lastName);
        }

        private void addGenresToBook(ResultSet rs, Book book) throws SQLException {
            Object[] genreIds = (Object[]) rs.getArray("genre_ids").getArray();
            Object[] genreTitles = (Object[]) rs.getArray("genre_titles").getArray();
            for (int i = 0; i < genreIds.length; i++) {
                if (genreIds[i] instanceof Long genreId && genreTitles[i] instanceof String genreTitle) {
                    book.addGenre(new Genre(genreId, genreTitle));
                }
            }
        }
    }
}