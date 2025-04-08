package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<CommentDto> findCommentByBookId(long id);

    Optional<CommentDto> findCommentById(long id);

    CommentDto update(long id, String text);

    CommentDto create(String text, long bookId);
}
