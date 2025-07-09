package com.galaxy13.hw.repository.jpa;

import com.galaxy13.hw.model.jpa.JpaBook;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaBookRepository extends JpaRepository<JpaBook, Long> {

    @EntityGraph(value = "author-genre-entity-graph")
    List<JpaBook> findAll();

    @EntityGraph(value = "author-entity-graph")
    List<JpaBook> findAllByOrderByIdAsc();

    @EntityGraph(value = "author-genre-entity-graph")
    Optional<JpaBook> findById(long id);

    Optional<JpaBook> findByTitle(String title);

    void deleteBookById(long id);
}
