package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.request.CommentRequestDto;
import com.galaxy13.hw.dto.response.CommentResponseDto;
import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.model.Book;
import com.galaxy13.hw.model.Comment;
import com.galaxy13.hw.repository.BookRepository;
import com.galaxy13.hw.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final Converter<Comment, CommentResponseDto> commentDtoMapper;

    @Transactional(readOnly = true)
    @Override
    public List<CommentResponseDto> findCommentByBookId(long id) {
        return commentRepository.findByBookId(id).stream().map(commentDtoMapper::convert).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public CommentResponseDto findCommentById(long id) {
        return commentRepository.findById(id).map(commentDtoMapper::convert).orElseThrow(() ->
                new EntityNotFoundException("Comment with id %d not found".formatted(id)));
    }

    @Transactional
    @Override
    public CommentResponseDto update(long id, CommentRequestDto commentDto) {
        Comment comment = commentRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("No comment found with id: %d".formatted(id))
        );
        comment.setText(commentDto.comment());
        return commentDtoMapper.convert(comment);
    }

    @Transactional
    @Override
    public CommentResponseDto create(CommentRequestDto commentDto) {
        Book book = bookRepository.findById(commentDto.bookId()).orElseThrow(
                () -> new EntityNotFoundException("Book not found"));
        Comment comment = new Comment();
        comment.setBook(book);
        comment.setText(commentDto.comment());
        return commentDtoMapper.convert(commentRepository.save(comment));
    }
}
