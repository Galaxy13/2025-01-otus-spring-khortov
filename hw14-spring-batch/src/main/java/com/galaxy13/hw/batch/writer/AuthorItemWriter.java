package com.galaxy13.hw.batch.writer;

import com.galaxy13.hw.model.jpa.JpaAuthor;
import com.galaxy13.hw.repository.jpa.JpaAuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorItemWriter implements ItemWriter<JpaAuthor> {

    private final JpaAuthorRepository jpaAuthorRepository;

    @Override
    public void write(Chunk<? extends JpaAuthor> chunk) {
        jpaAuthorRepository.saveAll(chunk.getItems());
    }
}
