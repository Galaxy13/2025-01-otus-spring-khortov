package com.galaxy13.hw.repository;

import com.galaxy13.hw.model.Author;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaAuthorRepository implements AuthorRepository {
    private final EntityManager em;

    @Override
    public List<Author> findAllAuthors() {
        TypedQuery<Author> query = em.createQuery("select a from Author a", Author.class);
        return query.getResultList();
    }

    @Override
    public Optional<Author> findById(long id) {
        return Optional.ofNullable(em.find(Author.class, id));
    }

    @Override
    public Optional<Author> findByFullName(String firstName, String lastName) {
        TypedQuery<Author> query = em.createQuery("select a from Author a " +
                "where a.firstName = :firstName and a.lastName = :lastName", Author.class);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Author save(Author author) {
        if (author.getId() == 0) {
            em.persist(author);
            return author;
        }
        return em.merge(author);
    }
}
