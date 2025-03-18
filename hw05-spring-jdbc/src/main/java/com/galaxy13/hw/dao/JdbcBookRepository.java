package com.galaxy13.hw.dao;

import com.galaxy13.hw.model.Author;
import com.galaxy13.hw.model.Book;
import com.galaxy13.hw.model.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    @Override
    public List<Book> findAllBooks() {
        Map<Long, Book> booksById = getAllBooksWithoutGenres();
        var genreRelations = getAllGenreBookRelations();
        var genres = genreRepository.findAllGenres();
        return null;
    }

    @Override
    public Book findBookById(long id) {
        return null;
    }

    @Override
    public Book findBookByTitle(String title) {
        return null;
    }

    @Override
    public List<Book> booksByAuthor(Author author) {
        return List.of();
    }

    @Override
    public List<Book> booksByGenre(Genre title) {
        return List.of();
    }

    @Override
    public Book addBook(Book book) {
        return null;
    }

    @Override
    public void deleteBookById(long id) {

    }

    private List<GenreBookRelation> getAllGenreBookRelations() {
        return jdbcTemplate.queryForList("select (id, genreTitle) from genres", Map.of(), GenreBookRelation.class);
    }

    private Map<Long, Book> getAllBooksWithoutGenres() {
        ;
        return jdbcTemplate.query("select (books.id, title, authorId) from books",
                Map.of(), new BookWithoutGenresResultSetExtractor());
    }

    private static class BookRowMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String title = rs.getString("title");
            long authorId = rs.getLong("author");
            long genreId = rs.getLong("genre");
        }
    }

    private record GenreBookRelation(long bookId, long genreId) {
    }

    private class BookWithoutGenresResultSetExtractor implements ResultSetExtractor<Map<Long, Book>> {
        @Override
        public Map<Long, Book> extractData(ResultSet rs) throws SQLException, DataAccessException {
            return Map.of();
        }

    }
}
