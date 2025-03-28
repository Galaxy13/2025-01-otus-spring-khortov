package com.galaxy13.hw.repository;

import com.galaxy13.hw.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    List<Comment> findCommentsByBookId(long id);

    Optional<Comment> findCommentById(long id);

    Comment saveComment(Comment comment);
}
