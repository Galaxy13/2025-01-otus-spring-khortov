package com.galaxy13.hw.repository;

import com.galaxy13.hw.model.Book;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {

    Flux<Book> findAllByOrderByIdAsc();
}
