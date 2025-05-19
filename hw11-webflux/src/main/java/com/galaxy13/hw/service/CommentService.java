package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.CommentDto;
import com.galaxy13.hw.dto.upsert.CommentUpsertDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CommentService {
    Flux<CommentDto> findCommentByBookId(String id);

    Mono<CommentDto> findCommentById(String id);

    Mono<CommentDto> update(CommentUpsertDto commentDto);

    Mono<CommentDto> create(CommentUpsertDto commentDto);
}
