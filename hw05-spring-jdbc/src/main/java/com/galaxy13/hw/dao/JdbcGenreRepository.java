package com.galaxy13.hw.dao;

import com.galaxy13.hw.model.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

@Repository
@RequiredArgsConstructor
public class JdbcGenreRepository implements GenreRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> findAllGenres() {
        return jdbcTemplate.query("""
                select (ID, GENRE_TITLE) FROM GENRES
                    ORDER BY ID""", new GenreRowMapper());
    }

    @Override
    public Optional<Genre> findGenreById(long id) {
        return Optional.ofNullable(getNullableResult(() -> namedParameterJdbcTemplate.queryForObject("""
                select (ID, GENRE_TITLE) FROM GENRES WHERE ID = :id
                    ORDER BY ID""", Map.of("id", id), new GenreRowMapper())));
    }

    @Override
    public List<Genre> findAllByIds(Set<Long> ids) {
        return namedParameterJdbcTemplate.query("""
            SELECT ID, GENRE_TITLE FROM GENRES AS G WHERE G.ID IN (:ids)
                ORDER BY G.ID""", Map.of("ids", ids), new GenreRowMapper());
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
