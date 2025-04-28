package com.galaxy13.hw.controller;

import com.galaxy13.hw.dto.mvc.CommentModelDto;
import com.galaxy13.hw.dto.service.CommentDto;
import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;

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

    @GetMapping("/comments/{comment_id}")
    public String editComment(@PathVariable("comment_id") long commentId, Model model) {
        CommentDto comment = commentService.findCommentById(commentId).orElseThrow(() ->
                new EntityNotFoundException("Comment with id %d not found".formatted(commentId)));
        model.addAttribute("comment", comment);
        return "comment_edit";
    }

    @PostMapping("/comments/{comment_id}")
    public String editComment(@PathVariable("comment_id") long commentId,
                              @Validated @ModelAttribute("comment") CommentModelDto comment) {
        commentService.update(commentId, comment.comment());
        return "redirect:/comments?book_id=" + comment.bookId();
    }

    @PostMapping("/comments")
    public String newCommentPage(@Validated @ModelAttribute("comment") CommentModelDto comment) {
        commentService.create(comment.comment(), comment.bookId());
        return "redirect:/comments?book_id=" + comment.bookId();
    }
}
