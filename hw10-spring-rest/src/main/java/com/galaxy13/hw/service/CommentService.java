package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.request.CommentRequestDto;
import com.galaxy13.hw.dto.response.CommentResponseDto;

import java.util.List;

public interface CommentService {
    List<CommentResponseDto> findCommentByBookId(long id);

    CommentResponseDto findCommentById(long id);

    CommentResponseDto update(long id, CommentRequestDto commentDto);

    CommentResponseDto create(CommentRequestDto commentDto);
}
