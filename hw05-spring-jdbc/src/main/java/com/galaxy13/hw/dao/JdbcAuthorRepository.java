package com.galaxy13.hw.dao;

import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.model.Author;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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

    @Override
    @Transactional
    public Author save(Author author) {
        if (author.getId() == 0) {
            return insert(author);
        }
        return update(author);
    }

    private Author insert(Author author) {
        var parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("firstName", author.getFirstName())
                .addValue("lastName", author.getLastName());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update("""
                        INSERT INTO AUTHORS
                            (FIRSTNAME, LASTNAME)
                        VALUES ( :firstName,  :lastName )""",
                parameterSource,
                keyHolder);
        author.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return author;
    }

    private Author update(Author author) {
        var parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", author.getId())
                .addValue("firstName", author.getFirstName())
                .addValue("lastName", author.getLastName());
        if (namedParameterJdbcTemplate.update("""
                UPDATE AUTHORS
                SET
                    FIRSTNAME = :firstName, LASTNAME = :lastName
                WHERE ID = :id
                """, parameterSource) == 0) {
            throw new EntityNotFoundException("Author with id " + author.getId() + " not found");
        }
        return author;
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
