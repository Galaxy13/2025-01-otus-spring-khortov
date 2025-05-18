package com.galaxy13.hw.service;

import com.galaxy13.hw.dto.BookDto;
import com.galaxy13.hw.dto.upsert.BookUpsertDto;
import com.galaxy13.hw.model.Author;
import com.galaxy13.hw.model.Genre;
import com.galaxy13.hw.repository.AuthorRepository;
import com.galaxy13.hw.repository.BookRepository;
import com.galaxy13.hw.repository.CommentRepository;
import com.galaxy13.hw.repository.GenreRepository;
import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.core.convert.converter.Converter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final CommentRepository commentRepository;

    private final Converter<Book, BookDto> bookDtoMapper;

    @Override
    public Mono<BookDto> findById(String id) {
        return bookRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Book not found")))
                .mapNotNull(bookDtoMapper::convert);
    }

    @Override
    public Flux<BookDto> findAll() {
        return bookRepository.findAllByOrderByIdAsc()
                .mapNotNull(bookDtoMapper::convert);
    }

    @Override
    public Mono<BookDto> create(BookUpsertDto newBook) {
        Mono<Author> authorMono = authorRepository.findById(newBook.authorId())
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Author not found")));

        Mono<List<Genre>> genresMono = genreRepository.findByIdIn(newBook.genreIds())
                .collectList()
                .flatMap(genres -> {
                    if (genres.isEmpty()) {
                        return Mono.error(new EntityNotFoundException("No valid genres found"));
                    }
                    return Mono.just(genres);
                });

        return Mono.zip(authorMono, genresMono)
                .flatMap(tuple -> {
                    Author author = tuple.getT1();
                    List<Genre> genres = tuple.getT2();

                    Book book = new Book();
                    book.setTitle(newBook.title());
                    book.setAuthor(author);
                    book.setGenres(genres);

                    return bookRepository.save(book);
                })
                .mapNotNull(bookDtoMapper::convert);
    }

    @Override
    public Mono<BookDto> update(BookUpsertDto updateBook) {
        return bookRepository.findById(updateBook.id())
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Book to update with provided id not found")))
                .flatMap(book -> Mono.zip(
                        authorRepository.findById(updateBook.authorId()),
                        genreRepository.findByIdIn(updateBook.genreIds()).collectList()
                ))
                .filter(tuple -> !tuple.getT2().isEmpty())
                .flatMap(tuple -> {
                    var author = tuple.getT1();
                    var genres = tuple.getT2();

                    Book saveBook = new Book(
                            updateBook.id(),
                            updateBook.title(),
                            author,
                            genres
                    );
                    return bookRepository.save(saveBook)
                            .mapNotNull(bookDtoMapper::convert);
                });
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return bookRepository.existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new EntityNotFoundException("Book not found with id: " + id));
                    }
                    return commentRepository.deleteByBookId(id)
                            .then(bookRepository.deleteById(id));
                });
    }
}
