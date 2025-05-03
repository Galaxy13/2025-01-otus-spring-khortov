package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.mvc.AuthorModelDto;
import com.galaxy13.hw.exception.EntityNotFoundException;
import org.springframework.core.convert.converter.Converter;
import com.galaxy13.hw.dto.service.AuthorDto;
import com.galaxy13.hw.repository.AuthorRepository;
import com.galaxy13.hw.model.Author;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    private final Converter<Author, AuthorDto> authorDtoMapper;

    @Transactional(readOnly = true)
    @Override
    public List<AuthorDto> findAllAuthors() {
        return authorRepository.findAll().stream().map(authorDtoMapper::convert).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public AuthorDto findAuthorById(long id) {
        return authorRepository.findById(id).map(authorDtoMapper::convert).orElseThrow(() ->
                new EntityNotFoundException("Author with id " + id + " not found"));
    }

    @Transactional
    @Override
    public AuthorDto insert(AuthorModelDto newAuthor) {
        Author author = authorRepository.save(new Author(0, newAuthor.firstName(), newAuthor.lastName()));
        return authorDtoMapper.convert(author);
    }

    @Transactional
    @Override
    public AuthorDto update(long id, AuthorModelDto updatedAuthor) {
        Author author = authorRepository.save(new Author(id, updatedAuthor.firstName(), updatedAuthor.lastName()));
        return authorDtoMapper.convert(author);
    }
}
