package com.galaxy13.hw.batch.processor;

import com.galaxy13.hw.batch.exception.ItemNotFoundException;
import com.galaxy13.hw.model.mongo.MongoComment;
import com.galaxy13.hw.model.jpa.JpaBook;
import com.galaxy13.hw.model.jpa.JpaComment;
import com.galaxy13.hw.repository.jpa.JpaBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentItemProcessor implements ItemProcessor<MongoComment, JpaComment> {

    private final JpaBookRepository jpaBookRepository;

    @Override
    public JpaComment process(MongoComment mongoComment) {
        JpaComment jpaComment = new JpaComment();
        jpaComment.setText(mongoComment.getText());

        JpaBook jpaBook = jpaBookRepository.findByTitle(mongoComment.getBook().getTitle())
                .orElseThrow(() -> new ItemNotFoundException("Book not found"));
        jpaComment.setBook(jpaBook);
        return jpaComment;
    }
}
