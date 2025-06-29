package com.galaxy13.hw.batch.writer;

import com.galaxy13.hw.model.jpa.JpaGenre;
import com.galaxy13.hw.repository.jpa.JpaGenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GenreItemWriter implements ItemWriter<JpaGenre> {

    private final JpaGenreRepository jpaGenreRepository;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void write(Chunk<? extends JpaGenre> chunk) {
        for (JpaGenre jpaGenre : chunk) {
            jpaGenreRepository.save(jpaGenre);
            jdbcTemplate.update(
                    "INSERT INTO temp_genre_id (mongo_id, jpa_id) VALUES ( ?, ? )",
                    jpaGenre.getMongoId(), jpaGenre.getId()
            );
        }
    }
}
