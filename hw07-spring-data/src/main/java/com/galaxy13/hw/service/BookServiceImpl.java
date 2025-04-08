package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.BookDto;
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
    public Optional<BookDto> findById(long id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.map(bookDtoMapper::convert);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream().map(bookDtoMapper::convert).toList();
    }

    @Transactional
    @Override
    public BookDto insert(String title, long authorId, Set<Long> genreIds) {
        Book insertBook = new Book();
        setAllBookFields(insertBook, title, findAuthorById(authorId), findGenreByIds(genreIds));
        return bookDtoMapper.convert(bookRepository.save(insertBook));
    }

    @Transactional
    @Override
    public BookDto update(long id, String title, long authorId, Set<Long> genreIds) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        Author author = findAuthorById(authorId);
        List<Genre> genres = findGenreByIds(genreIds);

        setAllBookFields(book, title, author, genres);

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

    private void setAllBookFields(Book book, String title, Author author, List<Genre> genreIds) {
        book.setTitle(title);
        book.setAuthor(author);
        book.setGenres(genreIds);
    }
}
