package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.CommentDto;
import com.galaxy13.hw.dto.upsert.CommentUpsertDto;
import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.model.Book;
import com.galaxy13.hw.model.Comment;
import com.galaxy13.hw.security.model.Role;
import com.galaxy13.hw.security.model.User;
import com.galaxy13.hw.security.service.CustomUserDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

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

    @MockitoBean
    private Authentication authentication;

    @MockitoBean
    private SecurityContext securityContext;

    private static List<Comment> getComments() {
        Book book = new Book();
        book.setId(1);
        Book book2 = new Book();
        book2.setId(2);
        Book book3 = new Book();
        book3.setId(3);
        return List.of(
                new Comment(1L, "C_1", "0", book),
                new Comment(2L, "C_2","0",  book),
                new Comment(3L, "C_3","0",  book2),
                new Comment(4L, "C_4", "0",  book2),
                new Comment(5L, "C_5", "0", book3),
                new Comment(6L, "C_6", "0",  book3)
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
        mockSecurityContext();
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
        mockSecurityContext();
        var actualComments = commentService.findCommentByBookId(bookId);
        assertThat(actualComments).usingRecursiveComparison().isEqualTo(commentsByAuthor.get(bookId).stream().map(
                this::toDto
        ).toList());
    }

    @DisplayName("Should insert new comment")
    @Test
    void shouldInsertNewComment() {
        mockSecurityContext();
        Book book = new Book();
        book.setId(2);
        var newComment = new CommentUpsertDto(0L, "New comment", book.getId());
        var actualComment = commentService.create(newComment);
        var expectedComment = new CommentDto(4, newComment.text(), newComment.bookId(), true);
        assertThat(actualComment)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedComment);
    }

    @DisplayName("Should not insert comment with non-existing book")
    @Test
    void shouldNotInsertNonExistingComment() {
        Book book = new Book();
        book.setId(-1);
        var newComment = new CommentUpsertDto(3L, "New comment", book.getId());
        assertThatThrownBy(() -> commentService.create(newComment)).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("Should update comment")
    @Test
    void shouldUpdateComment() {
        mockSecurityContext();
        Book book = new Book();
        book.setId(1);
        var commentIdx = 1L;
        var updateComment = new CommentUpsertDto(commentIdx, "New comment", book.getId());
        var actualComment = commentService.update(updateComment);
        var expectedComment = new CommentDto(1, updateComment.text(), updateComment.bookId());
        assertThat(actualComment).usingRecursiveComparison().isEqualTo(expectedComment);
    }

    @DisplayName("Should not update non-existing comment")
    @Test
    void shouldNotUpdateNonExistingComment() {
        Book book = new Book();
        book.setId(1);
        var newComment = new CommentUpsertDto(-1L, "New comment", book.getId());
        assertThatThrownBy(() -> commentService.update(newComment)).isInstanceOf(RuntimeException.class);
    }

    private CommentDto toDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(), comment.getBook().getId());
    }

    private void mockSecurityContext() {
        CustomUserDetails userDetails = new CustomUserDetails(
                new User(UUID.randomUUID(), "user", "12345", Role.ADMIN));
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }
}
