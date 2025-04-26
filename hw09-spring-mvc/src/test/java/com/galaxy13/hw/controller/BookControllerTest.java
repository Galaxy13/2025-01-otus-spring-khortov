package com.galaxy13.hw.controller;

import com.galaxy13.hw.dto.AuthorDto;
import com.galaxy13.hw.dto.BookDto;
import com.galaxy13.hw.dto.GenreDto;
import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.helper.UriBuilder;
import com.galaxy13.hw.service.AuthorService;
import com.galaxy13.hw.service.BookService;
import com.galaxy13.hw.service.GenreService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.galaxy13.hw.helper.TestData.getGenres;
import static com.galaxy13.hw.helper.TestData.getAuthors;
import static com.galaxy13.hw.helper.TestData.getBooks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@WebMvcTest(controllers = BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private AuthorService authorService;

    @MockitoBean
    private GenreService genreService;


    @Test
    void shouldReturnAllBooks() throws Exception {
        List<BookDto> expectedBooks = getBooks();
        Mockito.when(bookService.findAll()).thenReturn(expectedBooks);

        MvcResult result = mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("books"))
                .andExpect(view().name("book_storage"))
                .andReturn();

        List<BookDto> actualBooks = (List<BookDto>) result.getModelAndView().getModel().get("books");
        assertThat(actualBooks).usingRecursiveComparison().isEqualTo(expectedBooks);
    }

    @Test
    void shouldReturnViewToUpdateBook() throws Exception {
        BookDto expectedBook = getBooks().getFirst();
        Mockito.when(bookService.findById(expectedBook.getId())).thenReturn(Optional.of(expectedBook));
        Mockito.when(authorService.findAllAuthors()).thenReturn(getAuthors());
        Mockito.when(genreService.findAllGenres()).thenReturn(getGenres());

        MvcResult result = mvc.perform(get("/books/edit?id=" + expectedBook.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("book", "authors", "genres"))
                .andExpect(view().name("book_edit"))
                .andReturn();

        BookDto actualBook = (BookDto) result.getModelAndView().getModel().get("book");
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);

        List<AuthorDto> authors = (List<AuthorDto>) result.getModelAndView().getModel().get("authors");
        List<GenreDto> genres = (List<GenreDto>) result.getModelAndView().getModel().get("genres");
        assertThat(authors).usingRecursiveComparison().isEqualTo(getAuthors());
        assertThat(genres).usingRecursiveComparison().isEqualTo(getGenres());
    }

    @Test
    void shouldUpdateBook() throws Exception {
        BookDto book = getBooks().getFirst();
        AuthorDto newAuthor = getAuthors().get(1);
        List<GenreDto> newGenres = getGenres().subList(1, 3);
        BookDto expectedBook = new BookDto(book.getId(), "New title", newAuthor, newGenres);

        Mockito.when(bookService.update(expectedBook.getId(),
                expectedBook.getTitle(),
                expectedBook.getAuthor().getId(),
                expectedBook.getGenres().stream().map(GenreDto::getId).collect(Collectors.toSet())))
                .thenReturn(expectedBook);

        String uri = UriBuilder.fromStringUri("/books/edit").queryParam("id", expectedBook.getId())
                .queryParam("title", expectedBook.getTitle())
                .queryParam("authorId", expectedBook.getAuthor().getId())
                .queryParam("genreIds", expectedBook.getGenres().stream().map(GenreDto::getId).toList())
                .build();

        mvc.perform(post(uri)).andExpect(status().is3xxRedirection());

        Mockito.verify(bookService, Mockito.times(1)).update(expectedBook.getId(),
                        expectedBook.getTitle(),
                        expectedBook.getAuthor().getId(),
                        expectedBook.getGenres().stream().map(GenreDto::getId).collect(Collectors.toSet()));
    }

    @Test
    void shouldReturnViewToInsertBook() throws Exception {
        Mockito.when(authorService.findAllAuthors()).thenReturn(getAuthors());
        Mockito.when(genreService.findAllGenres()).thenReturn(getGenres());

        MvcResult result = mvc.perform(get("/books/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("authors", "genres"))
                .andExpect(view().name("book_new"))
                .andReturn();

        List<AuthorDto> authors = (List<AuthorDto>) result.getModelAndView().getModel().get("authors");
        List<GenreDto> genres = (List<GenreDto>) result.getModelAndView().getModel().get("genres");
        assertThat(authors).usingRecursiveComparison().isEqualTo(getAuthors());
        assertThat(genres).usingRecursiveComparison().isEqualTo(getGenres());

        Mockito.verify(authorService, Mockito.times(1)).findAllAuthors();
        Mockito.verify(genreService, Mockito.times(1)).findAllGenres();
    }

    @Test
    void shouldInsertNewBook() throws Exception {
        BookDto newBook = new BookDto(0, "New Book", getAuthors().getFirst(), getGenres().subList(2, 4));
        Mockito.when(bookService.insert(newBook.getTitle(),
                newBook.getAuthor().getId(),
                newBook.getGenres().stream().map(GenreDto::getId).collect(Collectors.toSet()))).thenReturn(newBook);

        String uri = UriBuilder.fromStringUri("/books/new")
                .queryParam("title", newBook.getTitle())
                .queryParam("authorId", newBook.getAuthor().getId())
                .queryParam("genreIds", newBook.getGenres().stream().map(GenreDto::getId).toList())
                .build();

        mvc.perform(post(uri))
                .andExpect(status().is3xxRedirection());

        Mockito.verify(bookService, Mockito.times(1)).insert(
                newBook.getTitle(),
                newBook.getAuthor().getId(),
                newBook.getGenres().stream().map(GenreDto::getId).collect(Collectors.toSet())
        );
    }

    @Test
    void shouldDeleteBook() throws Exception {
        BookDto bookToDelete = getBooks().getFirst();

        String uri = "/books/delete?id=" + bookToDelete.getId();
        mvc.perform(post(uri))
                .andExpect(status().is3xxRedirection());
        Mockito.verify(bookService, Mockito.times(1)).deleteById(bookToDelete.getId());
    }

    @Test
    void shouldThrowExceptionWhenBookNotFoundOnEdit() {
        BookDto nonExistingBook = new BookDto(0, "Non Existing Book", null, null);
        Mockito.when(bookService.findById(nonExistingBook.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> mvc.perform(get("/books/edit?id=" + nonExistingBook.getId())))
                .matches(e -> e.getCause() instanceof EntityNotFoundException);
    }


}
