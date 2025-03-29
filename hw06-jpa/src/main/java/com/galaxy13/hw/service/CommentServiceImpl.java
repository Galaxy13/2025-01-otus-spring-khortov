package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.CommentDto;
import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.model.Book;
import com.galaxy13.hw.model.Comment;
import com.galaxy13.hw.repository.BookRepository;
import com.galaxy13.hw.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findCommentByBookId(long id) {
        return commentRepository.findCommentsByBookId(id).stream().map(CommentDto::new).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<CommentDto> findCommentById(long id) {
        return commentRepository.findCommentById(id).map(CommentDto::new);
    }

    @Transactional
    @Override
    public CommentDto saveComment(long id, String text, long bookId) {
        if (text == null || text.isEmpty() || bookId == 0) {
            throw new IllegalArgumentException("No comment text and/or wrong book id");
        }
        Book book = bookRepository.findBookById(bookId).orElseThrow(
                () -> new EntityNotFoundException("Book not found"));
        Comment comment = commentRepository.saveComment(new Comment(id, text, book));
        return new CommentDto(comment);
    }
}
