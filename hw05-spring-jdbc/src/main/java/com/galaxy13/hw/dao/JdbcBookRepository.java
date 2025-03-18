package com.galaxy13.hw.dao;

import com.galaxy13.hw.model.Author;
import com.galaxy13.hw.model.Book;
import com.galaxy13.hw.model.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    private final JdbcTemplate jdbcTemplate;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    @Override
    public List<Book> findAllBooks() {
        Map<Long, Book> booksById = getAllBooksWithoutGenres();
        List<GenreBookRelation> genreRelations = getAllGenreBookRelations();
        List<Genre> genres = genreRepository.findAllGenres();
        Map<Long, Genre> genreMap = genresListTioMap(genres);
        return mergeInfoToBooks(booksById, genreRelations, genreMap);
    }

    @Override
    public Optional<Book> findBookById(long id) {
        return Optional.ofNullable(namedJdbcTemplate.queryForObject(
                "select (BOOKS.ID, TITLE, AUTHORID, A.FIRSTNAME, A.LASTNAME, G.ID, G.GENRETITLE)" +
                        " from BOOKS join PUBLIC.AUTHORS A on A.ID = BOOKS.AUTHORID" +
                        "join GENRESRELATIONSHIPS GR on BOOKS.ID = GR.BOOKID " +
                        "join PUBLIC.GENRES G on G.ID = GR.GENREID where BOOKID = :id",
                Map.of("id", id), new BookRowMapper()));
    }

    @Override
    public Optional<Book> findBookByTitle(String title) {
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
        return jdbcTemplate.query("select (bookId, genreId) from genresRelationships", new GenreBookMapper());
    }

    private List<GenreBookRelation> getGenreBookRelations(Long bookId) {
        return namedJdbcTemplate.query("select (BOOKID, genreId) from GENRESRELATIONSHIPS where bookId = :bookId",
                Map.of("bookId", bookId), new GenreBookMapper());
    }

    private Map<Long, Book> getAllBooksWithoutGenres() {
        return jdbcTemplate.query("select (books.id, title, authorId) from books",
                new BookWithoutGenresResultSetExtractor());
    }

    private List<GenreBookRelation> getBookGenresById(Long bookId) {
        return namedJdbcTemplate.query("select (bookId, genreId) from genresRelationships where bookId = :bookId",
                Map.of("bookId", bookId), new GenreBookMapper());
    }

    private List<Book> mergeInfoToBooks(Map<Long, Book> books, List<GenreBookRelation> genreBookRelations, Map<Long, Genre> genres) {
        genreBookRelations.forEach(genreRelation -> {
            Book book = books.get(genreRelation.bookId());
            book.getGenres().add(genres.get(genreRelation.genreId()));
        });
        return books.values().stream().toList();
    }

    private class BookRowMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            return null;
        }
    }


    private Map<Long, Genre> genresListTioMap(List<Genre> genres) {
        return genres.stream().collect(Collectors.toMap(Genre::getId, (genre) -> genre));
    }

    private record GenreBookRelation(long bookId, long genreId) {
    }

    private static class GenreBookMapper implements RowMapper<GenreBookRelation> {
        @Override
        public GenreBookRelation mapRow(ResultSet rs, int rowNum) throws SQLException {
            long bookId = rs.getLong("bookId");
            long genreId = rs.getLong("genreId");
            return new GenreBookRelation(bookId, genreId);
        }
    }

    private class BookWithoutGenresResultSetExtractor implements ResultSetExtractor<Map<Long, Book>> {
        @Override
        public Map<Long, Book> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, Book> books = new HashMap<>();
            while (rs.next()) {
                long id = rs.getLong("id");
                String title = rs.getString("title");
                long authorId = rs.getLong("author");

                Author author = authorRepository.findById(authorId);
                books.put(id, new Book(id, title, author));
            }
            return books;
        }
    }
}