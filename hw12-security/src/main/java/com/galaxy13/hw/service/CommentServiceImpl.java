package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.CommentDto;
import com.galaxy13.hw.dto.upsert.CommentUpsertDto;
import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.model.Book;
import com.galaxy13.hw.model.Comment;
import com.galaxy13.hw.repository.BookRepository;
import com.galaxy13.hw.repository.CommentRepository;
import com.galaxy13.hw.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final Converter<Comment, CommentDto> commentDtoMapper;

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findCommentByBookId(long id) {
        return commentRepository.findByBookId(id).stream()
                .map(this::handleDtoMapping).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public CommentDto findCommentById(long id) {
        return commentRepository.findById(id)
                .map(this::handleDtoMapping).orElseThrow(() ->
                new EntityNotFoundException("Comment with id %d not found".formatted(id)));
    }

    @Transactional
    @Override
    public CommentDto update(CommentUpsertDto commentDto) {
        long id = commentDto.id();
        Comment comment = commentRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("No comment found with id: %d".formatted(id))
        );
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.getId().equals(UUID.fromString(comment.getUserId()))) {
            throw new AccessDeniedException("You do not have permission to update this comment");
        }
        comment.setText(commentDto.text());
        return commentDtoMapper.convert(comment);
    }

    @Transactional
    @Override
    public CommentDto create(CommentUpsertDto commentDto) {
        Book book = bookRepository.findById(commentDto.bookId()).orElseThrow(
                () -> new EntityNotFoundException("Book not found"));
        CustomUserDetails user = (CustomUserDetails)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment comment = new Comment();
        comment.setBook(book);
        comment.setText(commentDto.text());
        comment.setUserId(user.getId().toString());
        CommentDto result = commentDtoMapper.convert(comment);
        result.setEditAllowed(true);
        return result;
    }

    private CommentDto handleDtoMapping(Comment comment) {
        CustomUserDetails userDetails = (CustomUserDetails)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CommentDto commentDto = commentDtoMapper.convert(comment);
        if (commentDto != null && comment.getUserId().equals(userDetails.getId().toString())){
            commentDto.setEditAllowed(true);
        }
        return commentDto;
    }
}
