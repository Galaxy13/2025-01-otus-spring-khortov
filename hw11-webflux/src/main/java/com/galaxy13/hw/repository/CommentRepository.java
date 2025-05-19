package com.galaxy13.hw.repository;

import com.galaxy13.hw.model.Comment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CommentRepository extends ReactiveMongoRepository<Comment, String> {
    Flux<Comment> findByBookId(String id);

    Mono<Void> deleteByBookId(String id);
}
