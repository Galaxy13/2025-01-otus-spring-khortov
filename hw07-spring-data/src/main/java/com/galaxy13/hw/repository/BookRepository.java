package com.galaxy13.hw.repository;

import com.galaxy13.hw.model.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph(value = "author-entity-graph")
    List<Book> findAll();

    @EntityGraph(value = "author-genre-entity-graph")
    Optional<Book> findById(long id);

    void deleteBookById(long id);
}
