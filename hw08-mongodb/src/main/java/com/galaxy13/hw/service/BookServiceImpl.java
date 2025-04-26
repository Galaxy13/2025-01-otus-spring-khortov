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

    @Override
    public Optional<BookDto> findById(String id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.map(bookDtoMapper::convert);
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream().map(bookDtoMapper::convert).toList();
    }

    @Override
    public BookDto insert(String title, String authorId, Set<String> genreIds) {
        Book insertBook = new Book();
        insertBook.setTitle(title);
        insertBook.setAuthor(findAuthorById(authorId));
        insertBook.setGenres(findGenreByIds(genreIds));
        return bookDtoMapper.convert(bookRepository.save(insertBook));
    }

    @Override
    public BookDto update(String id, String title, String authorId, Set<String> genreIds) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        Author author = findAuthorById(authorId);
        List<Genre> genres = findGenreByIds(genreIds);

        book.setTitle(title);
        book.setAuthor(author);
        book.setGenres(genres);

        return bookDtoMapper.convert(book);
    }

    @Override
    public void deleteById(String id) {
        bookRepository.deleteBookById(id);
    }

    private Author findAuthorById(String authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %s not found".formatted(authorId)));
    }

    private List<Genre> findGenreByIds(Set<String> genreIds) {
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
