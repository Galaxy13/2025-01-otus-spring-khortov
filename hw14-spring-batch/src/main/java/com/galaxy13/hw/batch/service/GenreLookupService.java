package com.galaxy13.hw.batch.service;

import com.galaxy13.hw.batch.exception.ItemNotFoundException;
import com.galaxy13.hw.batch.exception.SchemaViolationException;
import com.galaxy13.hw.model.jpa.JpaGenre;
import com.galaxy13.hw.repository.jpa.JpaGenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.IncorrectResultSetColumnCountException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@SuppressWarnings("java:S4449")
public class GenreLookupService {

    private final JpaGenreRepository genreRepository;

    private final JdbcTemplate jdbcTemplate;

    @Cacheable(cacheNames = "genres", key = "#mongoId")
    public JpaGenre findGenreByMongoId(String mongoId) {
        return genreRepository.findById(getGenreId(mongoId))
                .orElseThrow(() -> new ItemNotFoundException("Genre not found"));
    }

    private Long getGenreId(String mongoId) {
        try {
            return jdbcTemplate.queryForObject("SELECT jpa_id FROM temp_genre_id WHERE mongo_id = ?",
                    Long.class, mongoId);
        } catch (IncorrectResultSetColumnCountException | IncorrectResultSizeDataAccessException e) {
            throw new SchemaViolationException("Wrong number of columns in database or no entry", e);
        }
    }
}
