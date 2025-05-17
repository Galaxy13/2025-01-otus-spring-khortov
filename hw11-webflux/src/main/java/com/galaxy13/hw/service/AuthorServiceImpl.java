package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.upsert.AuthorUpsertDto;
import org.springframework.core.convert.converter.Converter;
import com.galaxy13.hw.dto.AuthorDto;
import com.galaxy13.hw.repository.AuthorRepository;
import com.galaxy13.hw.model.Author;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    private final Converter<Author, AuthorDto> authorDtoMapper;

    @Override
    public Flux<AuthorDto> findAllAuthors() {
        return authorRepository.findAllByOrderByIdAsc()
                .mapNotNull(authorDtoMapper::convert);
    }

    @Override
    public Mono<AuthorDto> findAuthorById(String id) {
        return authorRepository.findById(id).mapNotNull(authorDtoMapper::convert);
    }

    @Override
    public Mono<AuthorDto> create(AuthorUpsertDto authorDto) {
        Author author = new Author();
        author.setFirstName(authorDto.firstName());
        author.setLastName(authorDto.lastName());
        return authorRepository.save(author).mapNotNull(authorDtoMapper::convert);
    }

    @Override
    public Mono<AuthorDto> update(AuthorUpsertDto updatedAuthor) {
        String id = updatedAuthor.id();
        return authorRepository.findById(id)
                .flatMap( author -> {
                    author.setFirstName(updatedAuthor.firstName());
                    author.setLastName(updatedAuthor.lastName());
                    return authorRepository.save(author);
                }).mapNotNull(authorDtoMapper::convert);
    }
}
