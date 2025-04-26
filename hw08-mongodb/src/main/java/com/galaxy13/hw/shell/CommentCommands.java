package com.galaxy13.hw.shell;

import com.galaxy13.hw.dto.CommentDto;
import com.galaxy13.hw.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.command.annotation.Command;

import java.util.stream.Collectors;

@Command(command = "comment", description = "Comment commands", group = "Comment")
@RequiredArgsConstructor
public class CommentCommands {
    private final CommentService commentService;

    @Command(command = "find for book", alias = "ffb",
            description = "Find comments for book by book_id; params: {bookId: long}")
    public String findCommentsByBookId(String bookId) {
        return commentService.findCommentByBookId(bookId).stream()
                .map(CommentDto::toString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @Command(command = "find", alias = "cbid",
            description = "Find specific comment by id; params: {id: long}")
    public String findCommentById(String id) {
        return commentService.findCommentById(id).map(CommentDto::toString)
                .orElse("No comment with id: " + id + " found");
    }

    @Command(command = "save", alias = "cins",
            description = "Add new comment to book by id; params: {bookId: long} {text: String}")
    public String insertComment(String bookId, String text) {
        var savedCommentDto = commentService.create(text, bookId);
        return savedCommentDto.toString();
    }

    @Command(command = "update", alias = "cupd",
            description = "Update text of existing comment; params: {id: long} {text: String}")
    public String updateComment(String id, String text) {
        var updatedCommentDto = commentService.update(id, text);
        return updatedCommentDto.toString();
    }
}
