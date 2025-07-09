package com.galaxy13.hw.repository.jpa;

import com.galaxy13.hw.model.jpa.JpaGenre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface JpaGenreRepository extends JpaRepository<JpaGenre, Long> {
    List<JpaGenre> findByIdIn(Set<Long> ids);

    Optional<JpaGenre> findByName(String name);
}
