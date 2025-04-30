package com.galaxy13.hw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galaxy13.hw.dto.request.BookRequestDto;
import com.galaxy13.hw.dto.response.AuthorResponseDto;
import com.galaxy13.hw.dto.response.BookResponseDto;
import com.galaxy13.hw.dto.response.GenreResponseDto;
import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.service.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.galaxy13.hw.helper.TestData.getGenres;
import static com.galaxy13.hw.helper.TestData.getAuthors;
import static com.galaxy13.hw.helper.TestData.getBooks;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookService bookService;

    @Test
    void shouldReturnAllBooks() throws Exception {
        List<BookResponseDto> expectedBooks = getBooks();
        when(bookService.findAll()).thenReturn(expectedBooks);

        mvc.perform(get("/book"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedBooks)));
    }

    @Test
    void shouldReturnBookById() throws Exception {
        BookResponseDto expectedBook = getBooks().getFirst();
        when(bookService.findById(expectedBook.getId())).thenReturn(expectedBook);

        mvc.perform(get("/book/" + expectedBook.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedBook)));
    }

    @Test
    void shouldUpdateBook() throws Exception {
        BookResponseDto book = getBooks().getFirst();
        AuthorResponseDto newAuthor = getAuthors().get(1);
        List<GenreResponseDto> newGenres = getGenres().subList(1, 3);
        BookResponseDto expectedBook =
                new BookResponseDto(book.getId(), "New title", newAuthor, newGenres);
        BookRequestDto requestDto = new BookRequestDto(expectedBook.getTitle(),
                expectedBook.getAuthor().getId(),
                expectedBook.getGenres().stream().map(GenreResponseDto::getId).collect(Collectors.toSet()));

        when(bookService.update(book.getId(), requestDto))
                .thenReturn(expectedBook);


        mvc.perform(put("/book/" + book.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto)))
                        .andExpect(status().isOk())
                        .andExpect(content().json(objectMapper.writeValueAsString(expectedBook)));

        Mockito.verify(bookService, Mockito.times(1))
                .update(book.getId(), requestDto);
    }

    @Test
    void shouldInsertNewBook() throws Exception {
        BookResponseDto newBook = new BookResponseDto(4,
                "New Book",
                getAuthors().getFirst(), getGenres().subList(1, 3));
        BookRequestDto requestDto = new BookRequestDto("New Title", 1L, Set.of(1L, 2L));
        when(bookService.insert(requestDto)).thenReturn(newBook);

        mvc.perform(post("/book")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(newBook)));

        Mockito.verify(bookService, Mockito.times(1)).insert(requestDto);
    }

    @Test
    void shouldDeleteBook() throws Exception {
        BookResponseDto bookToDelete = getBooks().getFirst();

        String uri = "/book/" + bookToDelete.getId();
        mvc.perform(delete(uri))
                .andExpect(status().isOk());
        Mockito.verify(bookService, Mockito.times(1)).deleteById(bookToDelete.getId());
    }

    @Test
    void shouldThrowExceptionWhenBookNotFoundOnEdit() {
        when(bookService.findById(-1L)).thenThrow(EntityNotFoundException.class);

        assertThatThrownBy(() -> mvc.perform(get("/book/" + -1L)))
                .matches(e -> e.getCause() instanceof EntityNotFoundException);
    }


}
