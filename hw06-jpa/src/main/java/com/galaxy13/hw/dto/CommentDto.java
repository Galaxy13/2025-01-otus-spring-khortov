package com.galaxy13.hw.dto;

import com.galaxy13.hw.model.Comment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CommentDto {
    private final long id;

    private final String text;

    private final long bookId;

    public CommentDto(Comment comment){
        this.id = comment.getId();
        this.text = comment.getText();
        this.bookId = comment.getBookId();
    }
}
