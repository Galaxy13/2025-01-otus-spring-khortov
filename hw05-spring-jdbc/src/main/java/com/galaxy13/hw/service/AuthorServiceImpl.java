package com.galaxy13.hw.service;

import com.galaxy13.hw.repository.AuthorRepository;
import com.galaxy13.hw.model.Author;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    public List<Author> findAllAuthors() {
        return authorRepository.findAllAuthors();
    }

    @Override
    public Optional<Author> findAuthorById(long id) {
        return authorRepository.findById(id);
    }

    @Override
    public Author saveAuthor(long id, String firstName, String lastName) {
        if (firstName == null || lastName == null || firstName.isEmpty() || lastName.isEmpty()) {
            throw new IllegalArgumentException("Name and/or surname can't be empty");
        }
        Author author = new Author(id, firstName, lastName);
        return authorRepository.save(author);
    }
}
