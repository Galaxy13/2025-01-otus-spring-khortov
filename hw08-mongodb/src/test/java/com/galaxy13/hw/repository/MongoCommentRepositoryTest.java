package com.galaxy13.hw.repository;

import com.galaxy13.hw.AbstractBaseMongoTest;
import com.galaxy13.hw.mapper.CommentDtoMapper;
import com.galaxy13.hw.model.Comment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Mongo Comment Repository Test")
@DataMongoTest
@Import(CommentDtoMapper.class)
class MongoCommentRepositoryTest extends AbstractBaseMongoTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentDtoMapper mapper;

    @DisplayName("Should find comments by book id")
    @ParameterizedTest
    @ValueSource(strings = {"1", "2", "3"})
    void shouldFindCommentsByBookId(String id) {
        var actualComments = commentRepository.findByBookId(id).stream().map(mapper::convert).toList();
        var expectedComments = actualComments.stream().map(
                comment -> {
                    var comments = getMongoTemplate().findById(comment.getId(), Comment.class);
                    return mapper.convert(comments);
                })
                .toList();
        assertThat(actualComments).usingRecursiveComparison().isEqualTo(expectedComments);
        assertThat(actualComments).allSatisfy(comment -> assertThat(comment.getBookId()).isEqualTo(id));
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
