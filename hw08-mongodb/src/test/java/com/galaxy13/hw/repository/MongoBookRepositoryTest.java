package com.galaxy13.hw.repository;

import com.galaxy13.hw.AbstractBaseMongoTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import com.galaxy13.hw.model.Book;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Mongo Books Repository Test")
@DataMongoTest
class MongoBookRepositoryTest extends AbstractBaseMongoTest {

    @Autowired
    private BookRepository repository;

    @DisplayName("Should delete book by id")
    @Test
    void shouldDeleteBook() {
        assertThat(getMongoTemplate().findById("3", Book.class)).isNotNull();
        repository.deleteBookById("3");
        assertThat(getMongoTemplate().findById("3", Book.class)).isNull();
    }
}
