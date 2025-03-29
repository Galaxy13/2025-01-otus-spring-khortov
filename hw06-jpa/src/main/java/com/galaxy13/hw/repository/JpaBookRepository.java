package com.galaxy13.hw.repository;

import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.model.Book;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@RequiredArgsConstructor
@Repository
public class JpaBookRepository implements BookRepository {
    private final EntityManager em;

    @Override
    public List<Book> findAllBooks() {
        EntityGraph<?> entityGraph = em.getEntityGraph("author-entity-graph");
        TypedQuery<Book> query = em.createQuery("select b from Book b", Book.class);
        query.setHint(FETCH.getKey(), entityGraph);
        return query.getResultList();
    }

    @Override
    public Optional<Book> findBookById(long id) {
        EntityGraph<?> entityGraph = em.getEntityGraph("author-entity-graph");
        Map<String, Object> properties = Map.of(FETCH.getKey(), entityGraph);
        return Optional.ofNullable(em.find(Book.class, id, properties));
    }

    @Override
    public Book saveBook(Book book) {
        if (book.getId() == 0) {
            em.persist(book);
            return book;
        }
        return em.merge(book);
    }

    @Override
    public void deleteBookById(long id) {
        Book book = em.find(Book.class, id);
        if (book != null) {
            em.remove(book);
        } else {
            throw new EntityNotFoundException("Not found book for deletion with id: " + id);
        }
    }
}
