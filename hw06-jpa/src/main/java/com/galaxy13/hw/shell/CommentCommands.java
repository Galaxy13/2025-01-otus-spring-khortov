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
    public String findCommentsByBookId(long bookId) {
        return commentService.findCommentByBookId(bookId).stream()
                .map(CommentDto::toString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @Command(command = "find", alias = "cbid",
            description = "Find specific comment by id; params: {id: long}")
    public String findCommentById(long id) {
        return commentService.findCommentById(id).map(CommentDto::toString)
                .orElse("No comment with id: " + id + " found");
    }

    @Command(command = "save", alias = "cins",
            description = "Add new comment to book by id; params: {bookId: long} {text: String}")
    public String insertComment(long bookId, String text) {
        var savedCommentDto = commentService.saveComment(0, text, bookId);
        return savedCommentDto.toString();
    }

    @Command(command = "update", alias = "cupd",
            description = "Update existing comment; params: {id: long} {bookId: long} {text: String}")
    public String updateComment(long id, long bookId, String text) {
        var updatedCommentDto = commentService.saveComment(id, text, bookId);
        return updatedCommentDto.toString();
    }
}
