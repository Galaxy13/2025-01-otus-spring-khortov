package com.galaxy13.hw.repository;

import com.galaxy13.hw.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepository extends MongoRepository<Book, String> {
    void deleteBookById(String id);
}
