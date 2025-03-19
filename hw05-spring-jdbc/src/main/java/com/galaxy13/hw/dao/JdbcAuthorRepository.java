package com.galaxy13.hw.dao;

import com.galaxy13.hw.model.Author;
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
import java.util.function.Supplier;

@Repository
@RequiredArgsConstructor
public class JdbcAuthorRepository implements AuthorRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Author> findAllAuthors() {
        return jdbcTemplate.query("""
                SELECT
                    ID,
                    FIRSTNAME,
                    LASTNAME
                    from AUTHORS ORDER BY ID""", new AuthorRowMapper());
    }

    @Override
    public Optional<Author> findById(long id) {
        return getNullableResult(() -> Optional.ofNullable(namedParameterJdbcTemplate.queryForObject("""
                SELECT
                    ID,
                    FIRSTNAME,
                    LASTNAME
                    from AUTHORS
                    WHERE ID = :id
                    ORDER BY ID""", Map.of("id", id), new AuthorRowMapper())));
    }

    @Override
    public Optional<Author> findByFullName(String firstName, String lastName) {
        return getNullableResult(() -> Optional.ofNullable(namedParameterJdbcTemplate.queryForObject("""
                SELECT
                    ID,
                    FIRSTNAME,
                    LASTNAME
                    from AUTHORS
                    WHERE (FIRSTNAME = :firstName, LASTNAME = :lastName)
                    ORDER BY ID""",
                Map.of("firstName", firstName, "lastName", lastName), new AuthorRowMapper())));
    }

    private <T> T getNullableResult(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    private static class AuthorRowMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String firstName = rs.getString("firstname");
            String lastName = rs.getString("lastname");
            return new Author(id, firstName, lastName);
        }
    }
}
