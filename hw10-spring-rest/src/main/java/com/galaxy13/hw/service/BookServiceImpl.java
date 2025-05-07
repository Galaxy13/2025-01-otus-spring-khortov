package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.BookDto;
import com.galaxy13.hw.dto.upsert.BookUpsertDto;
import com.galaxy13.hw.model.Author;
import com.galaxy13.hw.model.Genre;
import com.galaxy13.hw.repository.AuthorRepository;
import com.galaxy13.hw.repository.BookRepository;
import com.galaxy13.hw.repository.GenreRepository;
import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.convert.converter.Converter;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final Converter<Book, BookDto> bookDtoMapper;

    @Transactional(readOnly = true)
    @Override
    public BookDto findById(long id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.map(bookDtoMapper::convert).orElseThrow(() ->
                new EntityNotFoundException("Book with id %d not found".formatted(id)));
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAllByOrderByIdAsc().stream().map(bookDtoMapper::convert).toList();
    }

    @Transactional
    @Override
    public BookDto create(BookUpsertDto newBook) {
        Book insertBook = new Book();
        insertBook.setTitle(newBook.title());
        insertBook.setAuthor(findAuthorById(newBook.authorId()));
        insertBook.setGenres(findGenreByIds(newBook.genreIds()));
        return bookDtoMapper.convert(bookRepository.save(insertBook));
    }

    @Transactional
    @Override
    public BookDto update(BookUpsertDto updateBook) {
        Book book = bookRepository.findById(updateBook.id())
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        Author author = findAuthorById(updateBook.authorId());
        List<Genre> genres = findGenreByIds(updateBook.genreIds());

        book.setTitle(updateBook.title());
        book.setAuthor(author);
        book.setGenres(genres);

        return bookDtoMapper.convert(book);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        bookRepository.deleteBookById(id);
    }

    private Author findAuthorById(long authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
    }

    private List<Genre> findGenreByIds(Set<Long> genreIds) {
        if (isEmpty(genreIds)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }

        List<Genre> genres = genreRepository.findByIdIn(genreIds);
        if (genres.isEmpty()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genreIds));
        }
        return genres;
    }
}
