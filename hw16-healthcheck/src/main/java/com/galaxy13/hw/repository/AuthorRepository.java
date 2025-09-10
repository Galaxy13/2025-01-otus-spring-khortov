package com.galaxy13.hw.repository;

import com.galaxy13.hw.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "authors")
public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findAllByOrderByIdAsc();

    Optional<Author> findByFirstNameAndLastName(String firstName, String lastName);
}
