package com.galaxy13.hw.health;

import com.galaxy13.hw.model.Book;
import com.galaxy13.hw.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HealthCheckBookRepository implements HealthIndicator {

    private final BookRepository bookRepository;

    @Override
    public Health health() {
        List<Book> books = bookRepository.findAll();
        if (books.isEmpty()) {
            return Health.unknown()
                    .withDetail("repository", "Book repository is empty!").build();
        }
        return Health.up().build();
    }
}
