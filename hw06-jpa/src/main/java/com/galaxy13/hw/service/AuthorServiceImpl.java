package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.AuthorDto;
import com.galaxy13.hw.repository.AuthorRepository;
import com.galaxy13.hw.model.Author;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Transactional(readOnly = true)
    @Override
    public List<AuthorDto> findAllAuthors() {
        return authorRepository.findAllAuthors().stream().map(AuthorDto::new).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<AuthorDto> findAuthorById(long id) {
        return authorRepository.findById(id).map(AuthorDto::new);
    }

    @Transactional
    @Override
    public AuthorDto saveAuthor(long id, String firstName, String lastName) {
        if (firstName == null || lastName == null || firstName.isEmpty() || lastName.isEmpty()) {
            throw new IllegalArgumentException("Name and/or surname can't be empty");
        }
        Author author = authorRepository.save(new Author(id, firstName, lastName));
        return new AuthorDto(author);
    }
}
