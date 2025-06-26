package com.galaxy13.hw.batch.writer;

import com.galaxy13.hw.model.jpa.JpaComment;
import com.galaxy13.hw.repository.jpa.JpaCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentItemWriter implements ItemWriter<JpaComment> {

    private final JpaCommentRepository jpaCommentRepository;

    @Override
    public void write(Chunk<? extends JpaComment> chunk) throws Exception {
        jpaCommentRepository.saveAll(chunk);
    }
}
