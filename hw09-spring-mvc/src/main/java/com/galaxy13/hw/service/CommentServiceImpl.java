package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.service.CommentDto;
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
import java.util.Optional;

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
    public Optional<CommentDto> findCommentById(long id) {
        return commentRepository.findById(id).map(commentDtoMapper::convert);
    }

    @Transactional
    @Override
    public CommentDto update(long id, String text) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("No comment text");
        }
        Comment comment = commentRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("No comment found with id: %d".formatted(id))
        );
        comment.setText(text);
        return commentDtoMapper.convert(comment);
    }

    @Transactional
    @Override
    public CommentDto create(String text, long bookId) {
        if (text == null || text.isEmpty() || bookId == 0) {
            throw new IllegalArgumentException("No comment text and/or wrong book id");
        }
        Book book = findBookById(bookId);
        Comment comment = commentRepository.save(new Comment(0, text, book));
        return commentDtoMapper.convert(comment);
    }

    private Book findBookById(long bookId) {
        return bookRepository.findById(bookId).orElseThrow(
                () -> new EntityNotFoundException("Book not found"));
    }
}
