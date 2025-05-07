package com.galaxy13.hw.repository;

import com.galaxy13.hw.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    List<Genre> findByIdIn(Set<Long> ids);
}
