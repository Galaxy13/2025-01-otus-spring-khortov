package com.galaxy13.hw.repository;

import com.galaxy13.hw.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByBookId(String id);

    void deleteCommentByBookId(String id);
}
