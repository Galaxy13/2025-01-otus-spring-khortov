package com.galaxy13.hw.dao;

import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.model.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Supplier;

@Repository
@RequiredArgsConstructor
public class JdbcGenreRepository implements GenreRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> findAllGenres() {
        return jdbcTemplate.query("""
                SELECT ID, GENRE_TITLE FROM GENRES
                    ORDER BY ID""", new GenreRowMapper());
    }

    @Override
    public Optional<Genre> findGenreById(long id) {
        return Optional.ofNullable(getNullableResult(() -> namedParameterJdbcTemplate.queryForObject("""
                SELECT ID, GENRE_TITLE FROM GENRES WHERE ID = :id
                    ORDER BY ID""", Map.of("id", id), new GenreRowMapper())));
    }

    @Override
    public List<Genre> findAllByIds(Set<Long> ids) {
        return namedParameterJdbcTemplate.query("""
            SELECT ID, GENRE_TITLE FROM GENRES AS G WHERE G.ID IN (:ids)
                ORDER BY G.ID""", Map.of("ids", ids), new GenreRowMapper());
    }

    @Override
    public Genre saveGenre(Genre genre) {
        if (genre.getId() == 0) {
            return insert(genre);
        }
        return update(genre);
    }

    private Genre insert(Genre genre) {
        KeyHolder holder = new GeneratedKeyHolder();
        var parameterSource = new MapSqlParameterSource().addValue("genreTitle", genre.getName());
        namedParameterJdbcTemplate.update("""
                INSERT INTO GENRES
                    ( GENRE_TITLE )
                VALUES ( :genreTitle )
                """, parameterSource, holder);
        genre.setId(Objects.requireNonNull(holder.getKey()).longValue());
        return genre;
    }

    private Genre update(Genre genre) {
        var parameterSource = new MapSqlParameterSource().addValue("genreTitle", genre.getName())
                .addValue("genreId", genre.getId());
        if (namedParameterJdbcTemplate.update("""
                UPDATE GENRES
                SET GENRE_TITLE = :genreTitle
                WHERE ID = :genreId""", parameterSource) == 0) {
            throw new EntityNotFoundException("Genre with id " + genre.getId() + " not found");
        }
        return genre;
    }

    private <T> T getNullableResult(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    private static class GenreRowMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String genreTitle = rs.getString("genre_title");
            return new Genre(id, genreTitle);
        }
    }
}
