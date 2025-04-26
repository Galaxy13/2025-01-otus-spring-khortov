package com.galaxy13.hw.repository;

import com.galaxy13.hw.model.Author;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AuthorRepository extends MongoRepository<Author, String> {
    Optional<Author> findByFirstNameAndLastName(String firstName, String lastName);
}
