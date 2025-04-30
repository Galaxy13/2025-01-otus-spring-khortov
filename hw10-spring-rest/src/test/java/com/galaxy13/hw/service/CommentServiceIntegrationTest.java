package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.request.CommentRequestDto;
import com.galaxy13.hw.dto.response.CommentResponseDto;
import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.model.Book;
import com.galaxy13.hw.model.Comment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("java:S5778")
@DisplayName("Integration Book service test")
@DataJpaTest
@Transactional(propagation = Propagation.NEVER)
@Sql(scripts = {"/cleanup.sql", "/data.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Import({CommentServiceImpl.class})
@ComponentScan("com.galaxy13.hw.mapper")
class CommentServiceIntegrationTest {

    private final Map<Long, List<Comment>> commentsByAuthor = bookIdToCommentMap();

    @Autowired
    private CommentService commentService;

    private static List<Comment> getComments() {
        Book book = new Book();
        book.setId(1);
        Book book2 = new Book();
        book2.setId(2);
        Book book3 = new Book();
        book3.setId(3);
        return List.of(
                new Comment(1L, "C_1", book),
                new Comment(2L, "C_2", book),
                new Comment(3L, "C_3", book2),
                new Comment(4L, "C_4", book2),
                new Comment(5L, "C_5", book3),
                new Comment(6L, "C_6", book3)
        );
    }

    private static Map<Long, List<Comment>> bookIdToCommentMap() {
        return getComments().stream().collect(Collectors.groupingBy(
                comment -> comment.getBook().getId()
        ));
    }

    @DisplayName("Should get comment by id")
    @ParameterizedTest
    @MethodSource("getComments")
    void shouldGetCommentById(Comment expectedComment) {
        var actualComment = commentService.findCommentById(expectedComment.getId());
        assertThat(actualComment).usingRecursiveComparison().isEqualTo(toDto(expectedComment));
    }

    @DisplayName("Should not find non-existing comment")
    @Test
    void shouldNotFindNonExistingComment() {
        assertThatThrownBy(() -> commentService.findCommentById(-1L)).isInstanceOf(EntityNotFoundException.class);
        assertThatThrownBy(() -> commentService.findCommentById(7L)).isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("Should find comments for book")
    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    void shouldFindCommentsForBook(Long bookId) {
        var actualComments = commentService.findCommentByBookId(bookId);
        assertThat(actualComments).usingRecursiveComparison().isEqualTo(commentsByAuthor.get(bookId).stream().map(
                this::toDto
        ).toList());
    }

    @DisplayName("Should insert new comment")
    @Test
    void shouldInsertNewComment() {
        Book book = new Book();
        book.setId(2);
        var newComment = new CommentRequestDto("New comment", book.getId());
        var actualComment = commentService.create(newComment);
        var expectedComment = new CommentResponseDto(4, newComment.comment(), newComment.bookId());
        assertThat(actualComment).matches(
                commentDto -> commentDto.getId() > 0
        ).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedComment);
    }

    @DisplayName("Should not insert comment with non-existing book")
    @Test
    void shouldNotInsertNonExistingComment() {
        Book book = new Book();
        book.setId(-1);
        var newComment = new CommentRequestDto("New comment", book.getId());
        assertThatThrownBy(() -> commentService.create(newComment)).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("Should update comment")
    @Test
    void shouldUpdateComment() {
        Book book = new Book();
        book.setId(1);
        var updateComment = new CommentRequestDto("New comment", book.getId());
        var actualComment = commentService.update(1, updateComment);
        var expectedComment = new CommentResponseDto(1, updateComment.comment(), updateComment.bookId());
        assertThat(actualComment).usingRecursiveComparison().isEqualTo(expectedComment);
    }

    @DisplayName("Should not update non-existing comment")
    @Test
    void shouldNotUpdateNonExistingComment() {
        Book book = new Book();
        book.setId(1);
        var newComment = new CommentRequestDto("New comment", book.getId());
        assertThatThrownBy(() -> commentService.update(-1, newComment)).isInstanceOf(RuntimeException.class);
    }

    private CommentResponseDto toDto(Comment comment) {
        return new CommentResponseDto(comment.getId(), comment.getText(), comment.getBook().getId());
    }
}
