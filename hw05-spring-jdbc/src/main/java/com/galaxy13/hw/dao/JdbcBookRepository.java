package com.galaxy13.hw.dao;

import com.galaxy13.hw.model.Author;
import com.galaxy13.hw.model.Book;
import com.galaxy13.hw.model.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Book> findAllBooks() {
        return List.of();
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

    private static class BookRowMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String title = rs.getString("title");
            String author = rs.getString("author");
            return null;
        }
    }
}
