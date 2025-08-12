package com.galaxy13.hw.repository;

import com.galaxy13.hw.model.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "books")
public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph(value = "author-entity-graph")
    List<Book> findAll();

    @EntityGraph(value = "author-entity-graph")
    List<Book> findAllByOrderByIdAsc();

    @EntityGraph(value = "author-genre-entity-graph")
    Optional<Book> findById(long id);

    void deleteBookById(long id);
}
