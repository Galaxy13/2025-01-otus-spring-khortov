package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.request.AuthorRequestDto;
import com.galaxy13.hw.exception.EntityNotFoundException;
import org.springframework.core.convert.converter.Converter;
import com.galaxy13.hw.dto.response.AuthorResponseDto;
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

    private final Converter<Author, AuthorResponseDto> authorDtoMapper;

    @Transactional(readOnly = true)
    @Override
    public List<AuthorResponseDto> findAllAuthors() {
        return authorRepository.findAll().stream().map(authorDtoMapper::convert).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public AuthorResponseDto findAuthorById(long id) {
        return authorRepository.findById(id).map(authorDtoMapper::convert).orElseThrow(() ->
                new EntityNotFoundException("Author with id " + id + " not found"));
    }

    @Transactional
    @Override
    public AuthorResponseDto insert(AuthorRequestDto authorDto) {
        Author author = new Author();
        author.setFirstName(authorDto.firstName());
        author.setLastName(authorDto.lastName());
        return authorDtoMapper.convert(authorRepository.save(author));
    }

    @Transactional
    @Override
    public AuthorResponseDto update(long id, AuthorRequestDto updatedAuthor) {
        Author existingAuthor = authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author with id " + id + " not found"));
        existingAuthor.setFirstName(updatedAuthor.firstName());
        existingAuthor.setLastName(updatedAuthor.lastName());
        return authorDtoMapper.convert(existingAuthor);
    }
}
