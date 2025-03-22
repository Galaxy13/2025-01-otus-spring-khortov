package com.galaxy13.hw.service;

import com.galaxy13.hw.repository.AuthorRepository;
import com.galaxy13.hw.repository.BookRepository;
import com.galaxy13.hw.repository.GenreRepository;
import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    @Override
    public Optional<Book> findById(long id) {
        return bookRepository.findBookById(id);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAllBooks();
    }

    @Override
    public Book insert(String title, long authorId, Set<Long> genreIds) {
        return save(0, title, authorId, genreIds);
    }

    @Override
    public Book update(long id, String title, long authorId, Set<Long> genreIds) {
        return save(id, title, authorId, genreIds);
    }

    @Override
    public void deleteById(long id) {
        bookRepository.deleteBookById(id);
    }

    private Book save(long id, String title, long authorId, Set<Long> genreIds) {
        if (isEmpty(genreIds)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }

        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        var genres = genreRepository.findAllByIds(genreIds);
        if (isEmpty(genres) || genreIds.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genreIds));
        }

        var book = new Book(id, title, author, genres);
        return bookRepository.saveBook(book);
    }
}
