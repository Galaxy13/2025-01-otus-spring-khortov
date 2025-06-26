package com.galaxy13.hw.batch.processor;

import com.galaxy13.hw.batch.exception.ItemNotFoundException;
import com.galaxy13.hw.model.mongo.MongoBook;
import com.galaxy13.hw.model.jpa.JpaAuthor;
import com.galaxy13.hw.model.jpa.JpaBook;
import com.galaxy13.hw.model.jpa.JpaGenre;
import com.galaxy13.hw.repository.jpa.JpaAuthorRepository;
import com.galaxy13.hw.repository.jpa.JpaGenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookItemProcessor implements ItemProcessor<MongoBook, JpaBook> {

    private final JpaAuthorRepository jpaAuthorRepository;

    private final JpaGenreRepository jpaGenreRepository;

    @Override
    public JpaBook process(MongoBook mongoBook) {
        JpaBook jpaBook = new JpaBook();
        jpaBook.setTitle(mongoBook.getTitle());

        JpaAuthor author = jpaAuthorRepository.findByFirstNameAndLastName(
                mongoBook.getAuthor().getFirstName(),
                mongoBook.getAuthor().getLastName()
        ).orElseThrow(() -> new ItemNotFoundException("Author not found"));
        jpaBook.setAuthor(author);

        List<JpaGenre> genres = mongoBook.getGenres().stream()
                .map(mongoGenre -> jpaGenreRepository.findByName(mongoGenre.getName())
                        .orElseThrow(() -> new ItemNotFoundException("Genre no found")))
                .toList();
        jpaBook.setGenres(genres);
        return jpaBook;
    }
}
