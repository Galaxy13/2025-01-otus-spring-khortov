package com.galaxy13.hw.controller;

import com.galaxy13.hw.dto.CommentDto;
import com.galaxy13.hw.dto.upsert.CommentUpsertDto;
import com.galaxy13.hw.exception.controller.MismatchedIdsException;
import com.galaxy13.hw.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public List<CommentDto> commentsByBookId(@RequestParam("book_id") long bookId) {
        return commentService.findCommentByBookId(bookId);
    }

    @GetMapping("/{comment_id}")
    public CommentDto getComment(@PathVariable("comment_id") long commentId) {
        return commentService.findCommentById(commentId);
    }

    @PutMapping("/{comment_id}")
    public CommentDto editComment(@PathVariable("comment_id") long id,
                                  @Validated @RequestBody CommentUpsertDto commentDto) {
        if (id != commentDto.id()) {
            throw new MismatchedIdsException(id, commentDto.id());
        }
        return commentService.update(commentDto);
    }

    @PostMapping
    public ResponseEntity<CommentDto> newComment(@Validated @RequestBody CommentUpsertDto commentDto) {
        return new ResponseEntity<>(commentService.create(commentDto), HttpStatus.CREATED);
    }
}
