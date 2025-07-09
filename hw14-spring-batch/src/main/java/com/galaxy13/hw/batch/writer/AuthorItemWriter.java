package com.galaxy13.hw.batch.writer;

import com.galaxy13.hw.model.jpa.JpaAuthor;
import com.galaxy13.hw.repository.jpa.JpaAuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorItemWriter implements ItemWriter<JpaAuthor> {

    private final JpaAuthorRepository jpaAuthorRepository;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void write(Chunk<? extends JpaAuthor> chunk) {
        for (JpaAuthor jpaAuthor : chunk) {
            JpaAuthor savedAuthor = jpaAuthorRepository.save(jpaAuthor);
            jdbcTemplate.update(
                    "INSERT INTO temp_author_id (mongo_id, jpa_id) VALUES ( ?, ? )",
                    jpaAuthor.getMongoId(), savedAuthor.getId()
            );
        }
    }
}
