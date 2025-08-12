package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.CommentDto;
import com.galaxy13.hw.dto.upsert.CommentUpsertDto;
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

    private final Converter<Comment, CommentDto> commentDtoMapper;

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findCommentByBookId(long id) {
        return commentRepository.findByBookId(id).stream().map(commentDtoMapper::convert).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public CommentDto findCommentById(long id) {
        return commentRepository.findById(id).map(commentDtoMapper::convert).orElseThrow(() ->
                new EntityNotFoundException("Comment with id %d not found".formatted(id)));
    }

    @Transactional
    @Override
    public CommentDto update(CommentUpsertDto commentDto) {
        long id = commentDto.id();
        Comment comment = commentRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("No comment found with id: %d".formatted(id))
        );
        comment.setText(commentDto.text());
        return commentDtoMapper.convert(comment);
    }

    @Transactional
    @Override
    public CommentDto create(CommentUpsertDto commentDto) {
        Book book = bookRepository.findById(commentDto.bookId()).orElseThrow(
                () -> new EntityNotFoundException("Book not found"));
        Comment comment = new Comment();
        comment.setBook(book);
        comment.setText(commentDto.text());
        return commentDtoMapper.convert(commentRepository.save(comment));
    }
}
