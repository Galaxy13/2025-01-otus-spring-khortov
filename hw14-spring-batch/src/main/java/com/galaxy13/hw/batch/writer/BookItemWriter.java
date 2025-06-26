package com.galaxy13.hw.batch.writer;

import com.galaxy13.hw.model.jpa.JpaBook;
import com.galaxy13.hw.repository.jpa.JpaBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookItemWriter implements ItemWriter<JpaBook> {

    private final JpaBookRepository jpaBookRepository;

    @Override
    public void write(Chunk<? extends JpaBook> chunk) throws Exception {
        jpaBookRepository.saveAll(chunk);
    }
}
