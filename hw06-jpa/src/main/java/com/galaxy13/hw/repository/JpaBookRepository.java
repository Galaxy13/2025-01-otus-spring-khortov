package com.galaxy13.hw.repository;

import com.galaxy13.hw.model.Book;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
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
        return Optional.ofNullable(em.find(Book.class, id));
    }

    @Override
    public Book saveBook(Book book) {
        if (book.getId() == 0){
            em.persist(book);
            return book;
        }
        return em.merge(book);
    }

    @Override
    public void deleteBookById(long id) {
        Query query = em.createQuery("delete from Book b where b.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
