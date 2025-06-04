package com.galaxy13.hw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galaxy13.hw.dto.AuthorDto;
import com.galaxy13.hw.dto.BookDto;
import com.galaxy13.hw.dto.GenreDto;
import com.galaxy13.hw.dto.upsert.BookUpsertDto;
import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.exception.controller.GlobalExceptionHandler;
import com.galaxy13.hw.service.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.galaxy13.hw.helper.TestData.getGenres;
import static com.galaxy13.hw.helper.TestData.getAuthors;
import static com.galaxy13.hw.helper.TestData.getBooks;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Book Controller Test (security disabled)")
@WebMvcTest(controllers = BookController.class)
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookService bookService;

    @Test
    void shouldReturnAllBooks() throws Exception {
        List<BookDto> expectedBooks = getBooks();
        when(bookService.findAll()).thenReturn(expectedBooks);

        mvc.perform(get("/api/v1/book"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedBooks)));
    }

    @Test
    void shouldReturnBookById() throws Exception {
        BookDto expectedBook = getBooks().getFirst();
        when(bookService.findById(expectedBook.id())).thenReturn(expectedBook);

        mvc.perform(get("/api/v1/book/" + expectedBook.id()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedBook)));
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
                .thenReturn(expectedBook);


        mvc.perform(put("/api/v1/book/" + book.id())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto)))
                        .andExpect(status().isOk())
                        .andExpect(content().json(objectMapper.writeValueAsString(expectedBook)));

        Mockito.verify(bookService, Mockito.times(1))
                .update(requestDto);
    }

    @Test
    void shouldInsertNewBook() throws Exception {
        BookDto newBook = new BookDto(4,
                "New Book",
                getAuthors().getFirst(), new HashSet<>(getGenres().subList(1, 3)));
        BookUpsertDto requestDto = new BookUpsertDto(0L, "New Title", 1L, Set.of(1L, 2L));
        when(bookService.create(requestDto)).thenReturn(newBook);

        mvc.perform(post("/api/v1/book")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(newBook)));

        Mockito.verify(bookService, Mockito.times(1)).create(requestDto);
    }

    @Test
    void shouldDeleteBook() throws Exception {
        BookDto bookToDelete = getBooks().getFirst();

        String uri = "/api/v1/book/" + bookToDelete.id();
        mvc.perform(delete(uri))
                .andExpect(status().isNoContent());
        Mockito.verify(bookService, Mockito.times(1)).deleteById(bookToDelete.id());
    }

    @Test
    void shouldThrowExceptionWhenBookNotFoundOnEdit() throws Exception {
        when(bookService.findById(-1L)).thenThrow(EntityNotFoundException.class);

        mvc.perform(get("/api/v1/book/" + -1L))
                .andExpect(status().isNotFound());

        when(bookService.findById(-2L)).thenThrow(NullPointerException.class);
        mvc.perform(get("/api/v1/book/" + -2L))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void shouldReturnBadRequestOnWrongCreateBody() throws Exception {
        mvc.perform(post("/api/v1/book").contentType("application/json")
                .content("""
                        {
                        "id": 0,
                        "authorId": 2,
                        "genreIds": [1, 3]
                        }"""))
                .andExpect(status().isBadRequest());
    }
}
