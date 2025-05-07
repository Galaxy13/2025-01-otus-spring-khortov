package com.galaxy13.hw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galaxy13.hw.dto.AuthorDto;
import com.galaxy13.hw.dto.upsert.AuthorUpsertDto;
import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.exception.controller.GlobalExceptionHandler;
import com.galaxy13.hw.service.AuthorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.galaxy13.hw.helper.TestData.getAuthors;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@WebMvcTest(controllers = {AuthorController.class,})
@Import(GlobalExceptionHandler.class)
class AuthorControllerTest {

    @MockitoBean
    private AuthorService authorService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllAuthors() throws Exception {
        when(authorService.findAllAuthors()).thenReturn(getAuthors());

        mvc.perform(get("/api/v1/author").accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(getAuthors())))
                .andReturn();
    }

    @Test
    void shouldReturnAuthorById() throws Exception {
        AuthorDto author = getAuthors().getFirst();
        AuthorDto expectedAuthor = new AuthorDto(author.id(), "New Name", "New Surname");
        when(authorService.findAuthorById(author.id())).thenReturn(expectedAuthor);

        mvc.perform(get("/api/v1/author/" + expectedAuthor.id()).accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedAuthor)))
                .andReturn();
    }

    @Test
    void shouldEditAuthor() throws Exception {
        AuthorDto author = getAuthors().getFirst();
        AuthorDto expectedAuthor = new AuthorDto(author.id(),
                "New Name", "New Surname");
        AuthorUpsertDto requestDto =
                new AuthorUpsertDto(expectedAuthor.id(), expectedAuthor.firstName(), expectedAuthor.lastName());
        when(authorService.update(requestDto)).thenReturn(expectedAuthor);

        mvc.perform(put("/api/v1/author/" + expectedAuthor.id())
                        .accept("application/json")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedAuthor)));

        verify(authorService, times(1))
                .update(requestDto);
    }

    @Test
    void shouldSaveNewAuthor() throws Exception {
        AuthorDto expectedAuthor = new AuthorDto(4L, "New Name", "New Surname");
        AuthorUpsertDto requestDto = new AuthorUpsertDto(expectedAuthor.id(),
                "New Name",
                "New Surname");
        when(authorService.create(requestDto)).thenReturn(expectedAuthor);

        String uri = "/api/v1/author";

        mvc.perform(post(uri).accept("application/json")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedAuthor)));

        verify(authorService, times(1)).create(requestDto);
    }

    @Test
    void shouldThrowExceptionOnEditNonExistingAuthor() throws Exception {
        AuthorDto nonExistingAuthor = new AuthorDto(0L, "Phillip" ,"Dick");
        AuthorUpsertDto requestDto =
                new AuthorUpsertDto(nonExistingAuthor.id(),
                        nonExistingAuthor.firstName(), nonExistingAuthor.lastName());
        when(authorService.update(requestDto))
        .thenThrow(EntityNotFoundException.class);
                mvc.perform(put("/api/v1/author/" + nonExistingAuthor.id())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto)))
                        .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestOnValidationError() throws Exception {
        mvc.perform(post("/api/v1/author").contentType("application/json")
                .content("""
                        {
                        "id": 0,
                        "lastName": "Marx"
                        }"""))
                .andExpect(status().isBadRequest());
    }
}
