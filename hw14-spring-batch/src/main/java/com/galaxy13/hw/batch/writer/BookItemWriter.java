package com.galaxy13.hw.batch.writer;

import com.galaxy13.hw.model.jpa.JpaBook;
import com.galaxy13.hw.repository.jpa.JpaBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookItemWriter implements ItemWriter<JpaBook> {

    private final JpaBookRepository jpaBookRepository;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void write(Chunk<? extends JpaBook> chunk) {
        for (JpaBook jpaBook : chunk) {
            JpaBook savedBook = jpaBookRepository.save(jpaBook);
            jdbcTemplate.update("INSERT INTO temp_book_id (mongo_id, jpa_id) VALUES ( ?, ? )",
                    jpaBook.getMongoId(), savedBook.getId());
        }
    }
}
