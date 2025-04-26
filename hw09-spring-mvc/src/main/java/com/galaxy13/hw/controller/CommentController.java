package com.galaxy13.hw.controller;

import com.galaxy13.hw.dto.CommentDto;
import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/comments")
    public String commentsByBookId(@RequestParam("book_id") long bookId, Model model) {
        List<CommentDto> comments = commentService.findCommentByBookId(bookId);
        model.addAttribute("comments", comments);
        return "book_comments";
    }

    @GetMapping("/comments/edit")
    public String editComment(@RequestParam("comment_id") long commentId, Model model) {
        CommentDto comment = commentService.findCommentById(commentId).orElseThrow(() ->
                new EntityNotFoundException("Comment with id %d not found".formatted(commentId)));
        model.addAttribute("comment", comment);
        return "comment_edit";
    }

    @PostMapping("comments/edit")
    public String editComment(@RequestParam("comment_id") long commentId,
                              @RequestParam("comment_text") String text,
                              @RequestParam("book_id") long bookId) {
        commentService.update(commentId, text);
        return "redirect:/comments?book_id=" + bookId;
    }

    @PostMapping("comments/new")
    public String newCommentPage(@RequestParam("comment_text") String text,
                                  @RequestParam("book_id") long bookId) {
        commentService.create(text, bookId);
        return "redirect:/comments?book_id=" + bookId;
    }
}
