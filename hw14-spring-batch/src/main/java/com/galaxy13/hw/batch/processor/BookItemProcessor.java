package com.galaxy13.hw.batch.processor;

import com.galaxy13.hw.batch.exception.ItemNotFoundException;
import com.galaxy13.hw.batch.exception.SchemaViolationException;
import com.galaxy13.hw.batch.service.GenreLookupService;
import com.galaxy13.hw.model.mongo.MongoBook;
import com.galaxy13.hw.model.jpa.JpaAuthor;
import com.galaxy13.hw.model.jpa.JpaBook;
import com.galaxy13.hw.model.jpa.JpaGenre;
import com.galaxy13.hw.repository.jpa.JpaAuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.IncorrectResultSetColumnCountException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@SuppressWarnings("java:S4449")
public class BookItemProcessor implements ItemProcessor<MongoBook, JpaBook> {

    private final JpaAuthorRepository jpaAuthorRepository;

    private final GenreLookupService genreLookupService;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public JpaBook process(MongoBook mongoBook) {
        JpaBook jpaBook = new JpaBook();
        jpaBook.setTitle(mongoBook.getTitle());

        JpaAuthor author = jpaAuthorRepository.findById(getAuthorId(mongoBook.getAuthor().getId()))
                        .orElseThrow(() -> new ItemNotFoundException("Author not found"));
        jpaBook.setAuthor(author);

        List<JpaGenre> genres = mongoBook.getGenres().stream()
                .map(mongoGenre -> genreLookupService.findGenreByMongoId(mongoGenre.getId()))
                .toList();
        jpaBook.setGenres(genres);
        jpaBook.setMongoId(mongoBook.getId());
        return jpaBook;
    }

    private Long getAuthorId(String mongoId) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT jpa_id FROM temp_author_id WHERE mongo_id = ?",
                    Long.class, mongoId);
        } catch (IncorrectResultSetColumnCountException | IncorrectResultSizeDataAccessException e) {
            throw new SchemaViolationException("Wrong number of columns in database or no entry", e);
        }
    }
}
