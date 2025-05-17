package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.CommentDto;
import com.galaxy13.hw.dto.upsert.CommentUpsertDto;
import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.model.Comment;
import com.galaxy13.hw.repository.BookRepository;
import com.galaxy13.hw.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final Converter<Comment, CommentDto> commentDtoMapper;

    @Override
    public Flux<CommentDto> findCommentByBookId(String id) {
        return commentRepository.findByBookId(id).mapNotNull(commentDtoMapper::convert);
    }

    @Override
    public Mono<CommentDto> findCommentById(String id) {
        return commentRepository.findById(id).mapNotNull(commentDtoMapper::convert);
    }

    @Override
    public Mono<CommentDto> update(CommentUpsertDto commentDto) {
        String id = commentDto.id();
        return commentRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Comment not found")))
                .flatMap(comment -> {
                    comment.setText(commentDto.text());
                    return commentRepository.save(comment);
                })
                .mapNotNull(commentDtoMapper::convert);
    }

    @Override
    public Mono<CommentDto> create(CommentUpsertDto commentDto) {
       return bookRepository.findById(commentDto.bookId())
               .switchIfEmpty(Mono.error(new EntityNotFoundException("Book for new comment not found")))
               .flatMap(book -> {
           Comment comment = new Comment();
           comment.setText(commentDto.text());
           comment.setBook(book);
           return commentRepository.save(comment);
       }).mapNotNull(commentDtoMapper::convert);
    }
}
