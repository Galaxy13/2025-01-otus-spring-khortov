package com.galaxy13.hw.repository;

import com.galaxy13.hw.model.Author;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {
    Flux<Author> findAllByOrderByIdAsc();

    Mono<Author> findByFirstNameAndLastName(String firstName, String lastName);
}
