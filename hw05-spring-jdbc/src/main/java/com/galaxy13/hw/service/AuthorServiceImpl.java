package com.galaxy13.hw.service;

import com.galaxy13.hw.dao.AuthorRepository;
import com.galaxy13.hw.model.Author;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    public List<Author> findAllAuthors() {
        return authorRepository.findAllAuthors();
    }
}
