package com.galaxy13.hw.repository;

import com.galaxy13.hw.AbstractBaseMongoTest;
import com.galaxy13.hw.model.Comment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Mongo Comment Repository Test")
@DataMongoTest
class MongoCommentRepositoryTest extends AbstractBaseMongoTest {

    @Autowired
    private CommentRepository commentRepository;

    @DisplayName("Should find comments by book id")
    @ParameterizedTest
    @ValueSource(strings = {"1", "2", "3"})
    void shouldFindCommentsByBookId(String id) {
        var actualComments = commentRepository.findByBookId(id);
        var expectedComments = actualComments.stream().map(
                comment -> getMongoTemplate().findById(comment.getId(), Comment.class)).toList();
        assertThat(actualComments).usingRecursiveComparison().isEqualTo(expectedComments);
        assertThat(actualComments).allSatisfy(comment -> assertThat(comment.getBook().getId()).isEqualTo(id));
    }

    @DisplayName("Should delete comments by book id")
    @Test
    void shouldDeleteCommentsByBookId() {
        var comments = commentRepository.findByBookId("1");
        assertThat(comments).isNotEmpty()
                        .hasSize(2);
        commentRepository.deleteCommentByBookId("1");
        comments.forEach(c -> assertThat(commentRepository.findById(c.getId())).isNotPresent());
    }
}
