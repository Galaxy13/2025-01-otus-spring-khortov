package com.galaxy13.hw.repository;

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
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("JPA Comment Repository Test")
@DataJpaTest
class JpaCommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager em;

    private static List<Long> getCommentIds() {
        return LongStream.range(1, 7).boxed().toList();
    }

    @DisplayName("Should find comment by id")
    @ParameterizedTest
    @MethodSource("getCommentIds")
    void shouldFindCommentById(long id) {
        var expectedComment = em.find(Comment.class, id);
        var actualComment = commentRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(" Test Comment with id " + id + " not found")
        );
        assertThat(actualComment).usingRecursiveComparison().isEqualTo(expectedComment);
    }

    @DisplayName("Should find comments by book id")
    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    void shouldFindCommentsByBookId(long id) {
        var actualComments = commentRepository.findByBookId(id);
        var expectedComments = actualComments.stream().map(
                comment -> em.find(Comment.class, comment.getId())).toList();
        assertThat(actualComments).usingRecursiveComparison().isEqualTo(expectedComments);
        assertThat(actualComments).allSatisfy(comment -> assertThat(comment.getBook().getId()).isEqualTo(id));
    }

    @DisplayName("Should not find non-existing comment")
    @ParameterizedTest
    @ValueSource(longs = {-1L, 0L, 7L})
    void shouldNotFindNonExistingComment(long id) {
        assertThat(commentRepository.findById(id)).isEmpty();
    }

    @DisplayName("Should insert new comment")
    @Test
    void shouldInsertNewComment() {
        var newComment = new Comment(0, "New comment", em.find(Book.class, 1L));
        var actualComment = commentRepository.save(newComment);
        assertThat(actualComment).matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(newComment);
    }

    @DisplayName("Should not insert comment with non-existing book")
    @Test
    void shouldNotInsertCommentWithNonExistingBook() {
        Book book = new Book();
        book.setId(-1L);
        var newComment = new Comment(0, "New comment", book);
        assertThatThrownBy(() -> commentRepository.save(newComment)).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("Should update comment")
    @Test
    void shouldUpdateComment() {
        var updateComment = new Comment(2, "New comment", em.find(Book.class, 3L));
        var actualComment = commentRepository.save(updateComment);
        assertThat(actualComment).usingRecursiveComparison().isEqualTo(updateComment);
        assertThat(actualComment).usingRecursiveComparison().isEqualTo(em.find(Comment.class, updateComment.getId()));
    }
}
