package com.galaxy13.hw.repository.jpa;

import com.galaxy13.hw.model.jpa.JpaComment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaCommentRepository extends JpaRepository<JpaComment, Long> {
    @EntityGraph(value = "book-entity-graph")
    List<JpaComment> findAll();

    @EntityGraph(value = "book-entity-graph")
    List<JpaComment> findByBookId(long id);
}
