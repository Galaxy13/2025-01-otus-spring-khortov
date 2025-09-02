package com.galaxy13.hw.health;

import com.galaxy13.hw.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookHealthIndicator implements HealthIndicator {

    private final BookRepository bookRepository;

    @Override
    public Health health() {
        long books = bookRepository.count();
        if (books == 0) {
            return Health.down()
                    .withDetail("repository", "Book repository is empty!").build();
        }
        return Health.up().build();
    }
}
