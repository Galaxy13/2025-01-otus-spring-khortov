package com.galaxy13.hw.batch.processor;

import com.galaxy13.hw.batch.exception.ItemNotFoundException;
import com.galaxy13.hw.batch.exception.SchemaViolationException;
import com.galaxy13.hw.model.mongo.MongoComment;
import com.galaxy13.hw.model.jpa.JpaBook;
import com.galaxy13.hw.model.jpa.JpaComment;
import com.galaxy13.hw.repository.jpa.JpaBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.IncorrectResultSetColumnCountException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@SuppressWarnings("java:S4449")
public class CommentItemProcessor implements ItemProcessor<MongoComment, JpaComment> {

    private final JpaBookRepository jpaBookRepository;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public JpaComment process(MongoComment mongoComment) {
        JpaComment jpaComment = new JpaComment();
        jpaComment.setText(mongoComment.getText());

        JpaBook jpaBook = jpaBookRepository.findById(getBookId(mongoComment.getBook().getId()))
                .orElseThrow(() -> new ItemNotFoundException("Book not found"));
        jpaComment.setBook(jpaBook);
        return jpaComment;
    }

    private Long getBookId(String mongoId) {
        try {
            return jdbcTemplate.queryForObject("SELECT jpa_id from temp_book_id WHERE mongo_id = ?",
                    Long.class, mongoId);
        } catch (IncorrectResultSetColumnCountException | IncorrectResultSizeDataAccessException e) {
            throw new SchemaViolationException("Wrong number of columns in database or no entry", e);
        }
    }
}
