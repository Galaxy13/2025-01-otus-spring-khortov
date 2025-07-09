package com.galaxy13.hw.repository.jpa;

import com.galaxy13.hw.model.jpa.JpaAuthor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaAuthorRepository extends JpaRepository<JpaAuthor, Long> {
    Optional<JpaAuthor> findByFirstNameAndLastName(String firstName, String lastName);
}
