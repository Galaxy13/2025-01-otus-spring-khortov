package com.galaxy13.hw.service;

import org.springframework.core.convert.converter.Converter;
import com.galaxy13.hw.dto.AuthorDto;
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

    private final Converter<Author, AuthorDto> authorDtoMapper;

    @Override
    public List<AuthorDto> findAllAuthors() {
        return authorRepository.findAll().stream().map(authorDtoMapper::convert).toList();
    }

    @Override
    public Optional<AuthorDto> findAuthorById(String id) {
        return authorRepository.findById(id).map(authorDtoMapper::convert);
    }

    @Override
    public AuthorDto saveAuthor(String id, String firstName, String lastName) {
        if (firstName == null || lastName == null || firstName.isEmpty() || lastName.isEmpty()) {
            throw new IllegalArgumentException("Name and/or surname can't be empty");
        }
        Author author = authorRepository.save(new Author(id, firstName, lastName));
        return authorDtoMapper.convert(author);
    }
}
