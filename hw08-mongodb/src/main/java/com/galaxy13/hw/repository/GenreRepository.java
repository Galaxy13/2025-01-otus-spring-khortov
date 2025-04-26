package com.galaxy13.hw.repository;

import com.galaxy13.hw.model.Genre;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Set;

public interface GenreRepository extends MongoRepository<Genre, String> {
    List<Genre> findByIdIn(Set<String> ids);
}
