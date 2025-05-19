package com.galaxy13.hw.repository;

import com.galaxy13.hw.model.Genre;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.util.Set;

public interface GenreRepository extends ReactiveMongoRepository<Genre, String> {
    Flux<Genre> findByIdIn(Set<String> ids);
}
