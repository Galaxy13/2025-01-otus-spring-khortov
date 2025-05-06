package com.galaxy13.hw.controller;

import com.galaxy13.hw.dto.request.CommentRequestDto;
import com.galaxy13.hw.dto.response.CommentResponseDto;
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

    @GetMapping("/")
    public List<CommentResponseDto> commentsByBookId(@RequestParam("book_id") long bookId) {
        return commentService.findCommentByBookId(bookId);
    }

    @GetMapping("/{comment_id}")
    public CommentResponseDto getComment(@PathVariable("comment_id") long commentId) {
        return commentService.findCommentById(commentId);
    }

    @PutMapping("/{comment_id}")
    public CommentResponseDto editComment(@PathVariable("comment_id") long commentId,
                                          @Validated @RequestBody CommentRequestDto comment) {
        return commentService.update(commentId, comment);
    }

    @PostMapping("/")
    public ResponseEntity<CommentResponseDto> newComment(@Validated @RequestBody CommentRequestDto comment) {
        return new ResponseEntity<>(commentService.create(comment), HttpStatus.CREATED);
    }
}
