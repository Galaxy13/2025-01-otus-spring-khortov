package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.CommentDto;
import com.galaxy13.hw.dto.upsert.CommentUpsertDto;
import com.galaxy13.hw.exception.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Integration Comment service test")
@DataMongoTest
@Import({CommentServiceImpl.class})
@ComponentScan("com.galaxy13.hw.mapper")
class CommentServiceIntegrationTest extends AbstractBaseMongoTest {

    private final Map<String, List<CommentDto>> commentsByBook = bookIdToCommentMap();

    @Autowired
    private CommentService commentService;

    private static List<CommentDto> getComments() {
        String book = "1";
        String book2 = "2";
        String book3 = "3";
        return List.of(
                new CommentDto("1", "C_1", book),
                new CommentDto("2","C_2", book),
                new CommentDto("3", "C_3", book2),
                new CommentDto("4", "C_4", book2),
                new CommentDto("5", "C_5", book3),
                new CommentDto("6", "C_6", book3)
        );
    }

    private static Map<String, List<CommentDto>> bookIdToCommentMap() {
        return getComments().stream().collect(Collectors.groupingBy(
                CommentDto::bookId
        ));
    }

    @DisplayName("Should get comment by id")
    @ParameterizedTest
    @MethodSource("getComments")
    void shouldGetCommentById(CommentDto expectedComment) {
        var actualComment = commentService.findCommentById(expectedComment.id());
        StepVerifier.create(actualComment)
                .expectNext(expectedComment)
                .verifyComplete();
    }

    @DisplayName("Should not find non-existing comment")
    @Test
    void shouldNotFindNonExistingComment() {
        StepVerifier.create(commentService.findCommentById("-1"))
                .verifyError(EntityNotFoundException.class);
    }

    @DisplayName("Should find comments for book")
    @ParameterizedTest
    @ValueSource(strings = {"1", "2", "3"})
    void shouldFindCommentsForBook(String bookId) {
        var actualComments = commentService.findCommentByBookId(bookId);
        var expectedComments = commentsByBook.get(bookId);
        StepVerifier.create(actualComments.collectList())
                .assertNext(actualList -> {
                    assertEquals(expectedComments.size(), actualList.size());
                    assertTrue(actualList.containsAll(expectedComments));
                })
                .verifyComplete();
    }

    @DisplayName("Should insert new comment")
    @Test
    void shouldInsertNewComment() {
        String bookId = "2";
        var newComment = new CommentUpsertDto("0", "New comment", bookId);
        var actualComment = commentService.create(newComment);
        var expectedComment = new CommentDto(newComment.id(), newComment.text(), newComment.bookId());
        StepVerifier.create(actualComment)
                .assertNext(commentDto -> {
                    assertThat(expectedComment.text()).isEqualTo(commentDto.text());
                    assertThat(expectedComment.bookId()).isEqualTo(commentDto.bookId());
                })
                .verifyComplete();
    }

    @DisplayName("Should not insert comment with non-existing book")
    @Test
    void shouldNotInsertNonExistingComment() {
        String nonExistingBookId = "-1";
        var newComment = new CommentUpsertDto("0", "New comment", nonExistingBookId);
        StepVerifier.create(commentService.create(newComment))
                .verifyError(EntityNotFoundException.class);
    }

    @DisplayName("Should update comment")
    @Test
    void shouldUpdateComment() {
        CommentDto commentToUpdate = commentsByBook.get("1").getFirst();
        var updateComment = new CommentUpsertDto(commentToUpdate.id(),
                "New comment Text", commentToUpdate.bookId());
        var expectedComment = new CommentDto(updateComment.id(), updateComment.text(), updateComment.bookId());
        StepVerifier.create(commentService.update(updateComment))
                .expectNext(expectedComment)
                .verifyComplete();
    }

    @DisplayName("Should not update non-existing comment")
    @Test
    void shouldNotUpdateNonExistingComment() {
        String bookId = "1";
        var newComment = new CommentUpsertDto("-1", "New comment", bookId);
        StepVerifier.create(commentService.update(newComment))
                .verifyError(EntityNotFoundException.class);
    }
}
