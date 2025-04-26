package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.CommentDto;
import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.model.Book;
import com.galaxy13.hw.model.Comment;
import com.galaxy13.hw.repository.BookRepository;
import com.galaxy13.hw.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final Converter<Comment, CommentDto> commentDtoMapper;

    @Override
    public List<CommentDto> findCommentByBookId(String id) {
        return commentRepository.findByBookId(id).stream().map(commentDtoMapper::convert).toList();
    }

    @Override
    public Optional<CommentDto> findCommentById(String id) {
        return commentRepository.findById(id).map(commentDtoMapper::convert);
    }

    @Override
    public CommentDto update(String id, String text) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("No comment text");
        }
        Comment comment = commentRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("No comment found with id: %s".formatted(id))
        );
        comment.setText(text);
        return commentDtoMapper.convert(comment);
    }

    @Override
    public CommentDto create(String text, String bookId) {
        if (text == null || text.isEmpty() || bookId.isEmpty()) {
            throw new IllegalArgumentException("No comment text and/or wrong book id");
        }
        Book book = findBookById(bookId);
        Comment comment = commentRepository.save(new Comment(null, text, book));
        return commentDtoMapper.convert(comment);
    }

    private Book findBookById(String bookId) {
        return bookRepository.findById(bookId).orElseThrow(
                () -> new EntityNotFoundException("Book not found"));
    }
}
