package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<CommentDto> findCommentByBookId(String id);

    Optional<CommentDto> findCommentById(String id);

    CommentDto update(String id, String text);

    CommentDto create(String text, String bookId);
}
