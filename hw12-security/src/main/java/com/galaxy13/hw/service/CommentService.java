package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.CommentDto;
import com.galaxy13.hw.dto.upsert.CommentUpsertDto;

import java.util.List;

public interface CommentService {
    List<CommentDto> findCommentByBookId(long id);

    CommentDto findCommentById(long id);

    CommentDto update(CommentUpsertDto commentDto);

    CommentDto create(CommentUpsertDto commentDto);
}
