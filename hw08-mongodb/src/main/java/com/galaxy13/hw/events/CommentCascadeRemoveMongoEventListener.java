package com.galaxy13.hw.events;

import com.galaxy13.hw.model.Book;
import com.galaxy13.hw.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentCascadeRemoveMongoEventListener extends AbstractMongoEventListener<Book> {

    private final CommentRepository commentRepository;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Book> event) {
        var id = String.valueOf(event.getSource().get("_id"));
        commentRepository.deleteCommentByBookId(id);
    }
}
