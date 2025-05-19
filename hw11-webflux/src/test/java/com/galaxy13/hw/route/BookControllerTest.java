package com.galaxy13.hw.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galaxy13.hw.dto.AuthorDto;
import com.galaxy13.hw.dto.BookDto;
import com.galaxy13.hw.dto.GenreDto;
import com.galaxy13.hw.dto.upsert.BookUpsertDto;
import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.route.config.BookRouteConfig;
import com.galaxy13.hw.route.handler.BookHandler;
import com.galaxy13.hw.route.validator.UpsertDtoValidator;
import com.galaxy13.hw.service.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.galaxy13.hw.helper.TestData.getGenres;
import static com.galaxy13.hw.helper.TestData.getAuthors;
import static com.galaxy13.hw.helper.TestData.getBooks;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = BookRouteConfig.class)
@Import({BookHandler.class, UpsertDtoValidator.class,})
@ComponentScan("com.galaxy13.hw.exception.handler")
class BookControllerTest {

    @Autowired
    private WebTestClient wtc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookService bookService;

    @Test
    void shouldReturnAllBooks() throws Exception {
        List<BookDto> expectedBooks = getBooks();
        when(bookService.findAll()).thenReturn(Flux.fromIterable(expectedBooks));

        wtc.get().uri("/flux/book").exchange()
                .expectStatus().isOk()
                .expectBody()
                .json(objectMapper.writeValueAsString(expectedBooks));
    }

    @Test
    void shouldReturnBookById() throws Exception {
        BookDto expectedBook = getBooks().getFirst();
        when(bookService.findById(expectedBook.id())).thenReturn(Mono.just(expectedBook));

        wtc.get().uri("/flux/book/" + expectedBook.id()).exchange()
                .expectStatus().isOk()
                .expectBody()
                .json(objectMapper.writeValueAsString(expectedBook));
    }

    @Test
    void shouldUpdateBook() throws Exception {
        BookDto book = getBooks().getFirst();
        AuthorDto newAuthor = getAuthors().get(1);
        List<GenreDto> newGenres = getGenres().subList(1, 3);
        BookDto expectedBook =
                new BookDto(book.id(), "New title", newAuthor, new HashSet<>(newGenres));
        BookUpsertDto requestDto = new BookUpsertDto(
                expectedBook.id(), expectedBook.title(),
                expectedBook.author().id(),
                expectedBook.genres().stream().map(GenreDto::id).collect(Collectors.toSet()));

        when(bookService.update(requestDto))
                .thenReturn(Mono.just(expectedBook));

        wtc.put().uri("/flux/book/" + expectedBook.id()).contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(requestDto))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json(objectMapper.writeValueAsString(expectedBook));

        Mockito.verify(bookService, Mockito.times(1))
                .update(requestDto);
    }

    @Test
    void shouldInsertNewBook() throws Exception {
        BookDto expected = new BookDto("",
                "New Book",
                getAuthors().getFirst(), new HashSet<>(getGenres().subList(1, 3)));
        BookUpsertDto requestDto = new BookUpsertDto("0", "New Title", "1", Set.of("1", "2"));
        when(bookService.create(requestDto)).thenReturn(Mono.just(expected));

        wtc.post().uri("/flux/book").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(requestDto))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .json(objectMapper.writeValueAsString(expected));

        Mockito.verify(bookService, Mockito.times(1)).create(requestDto);
    }

    @Test
    void shouldDeleteBook() {
        BookDto bookToDelete = getBooks().getFirst();

        when(bookService.deleteById(bookToDelete.id())).thenReturn(Mono.empty());

        String uri = "/flux/book/" + bookToDelete.id();
        wtc.delete().uri(uri).exchange()
                .expectStatus().isNoContent();

        Mockito.verify(bookService, Mockito.times(1)).deleteById(bookToDelete.id());
    }

    @Test
    void shouldThrowExceptionWhenBookNotFoundOnEdit() {
        when(bookService.findById("-1")).thenReturn(Mono.error(new EntityNotFoundException("")));
        wtc.get().uri("/flux/book/-1").exchange()
                .expectStatus().isNotFound();

        when(bookService.findById("-2")).thenReturn(Mono.error(new NullPointerException()));
        wtc.get().uri("/flux/book/-2").exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void shouldReturnBadRequestOnWrongCreateBody() throws Exception {
        wtc.post().uri("/flux/book").contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                        "id": 0,
                        "authorId": 2,
                        "genreIds": [1, 3]
                        }""")
                .exchange()
                .expectStatus().isBadRequest();
    }
}
