package com.galaxy13.hw.repository;

import com.galaxy13.hw.model.Genre;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class JpaGenreRepository implements GenreRepository {
    private final EntityManager em;

    @Transactional(readOnly = true)
    @Override
    public List<Genre> findAllGenres() {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g", Genre.class);
        return query.getResultList();
    }

    @Override
    public Optional<Genre> findGenreById(long id) {
        return Optional.ofNullable(em.find(Genre.class, id));
    }

    @Override
    public List<Genre> findAllByIds(Set<Long> ids) {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g where g.id in (:ids)", Genre.class);
        query.setParameter("ids", ids);
        return query.getResultList();
    }

    @Override
    public Genre saveGenre(Genre genre) {
        if (genre.getId() == 0){
            em.persist(genre);
            return genre;
        }
        return em.merge(genre);
    }
}
