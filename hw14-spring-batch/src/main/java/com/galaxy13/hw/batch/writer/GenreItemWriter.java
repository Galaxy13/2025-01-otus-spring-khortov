package com.galaxy13.hw.batch.writer;

import com.galaxy13.hw.model.jpa.JpaGenre;
import com.galaxy13.hw.repository.jpa.JpaGenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GenreItemWriter implements ItemWriter<JpaGenre> {

    private final JpaGenreRepository jpaGenreRepository;

    @Override
    public void write(Chunk<? extends JpaGenre> chunk) {
        jpaGenreRepository.saveAll(chunk);
    }
}
