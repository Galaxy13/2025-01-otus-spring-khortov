package com.galaxy13.hw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galaxy13.hw.dto.request.AuthorRequestDto;
import com.galaxy13.hw.dto.response.AuthorResponseDto;
import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.service.AuthorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.galaxy13.hw.helper.TestData.getAuthors;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@WebMvcTest(controllers = AuthorController.class)
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

        mvc.perform(get("/author").accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(getAuthors())))
                .andReturn();
    }

    @Test
    void shouldReturnAuthorById() throws Exception {
        AuthorResponseDto author = getAuthors().getFirst();
        AuthorResponseDto expectedAuthor = new AuthorResponseDto(author.getId(), "New Name", "New Surname");
        when(authorService.findAuthorById(author.getId())).thenReturn(expectedAuthor);

        mvc.perform(get("/author/" + expectedAuthor.getId()).accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedAuthor)))
                .andReturn();
    }

    @Test
    void shouldEditAuthor() throws Exception {
        AuthorResponseDto author = getAuthors().getFirst();
        AuthorResponseDto expectedAuthor = new AuthorResponseDto(author.getId(),
                "New Name", "New Surname");
        AuthorRequestDto requestDto =
                new AuthorRequestDto(expectedAuthor.getFirstName(), expectedAuthor.getLastName());
        when(authorService.update(expectedAuthor.getId(), requestDto)).thenReturn(expectedAuthor);

        mvc.perform(put("/author/" + expectedAuthor.getId())
                        .accept("application/json")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedAuthor)));

        verify(authorService, times(1))
                .update(expectedAuthor.getId(), requestDto);
    }

    @Test
    void shouldSaveNewAuthor() throws Exception {
        AuthorResponseDto expectedAuthor = new AuthorResponseDto(4, "New Name", "New Surname");
        AuthorRequestDto requestDto = new AuthorRequestDto("New Name", "New Surname");
        when(authorService.insert(requestDto)).thenReturn(expectedAuthor);

        String uri = "/author";

        mvc.perform(post(uri).accept("application/json")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedAuthor)));

        verify(authorService, times(1)).insert(requestDto);
    }

    @Test
    void shouldThrowExceptionOnEditNonExistingAuthor() {
        AuthorResponseDto nonExistingAuthor = new AuthorResponseDto(0, "Phillip" ,"Dick");
        AuthorRequestDto requestDto =
                new AuthorRequestDto(nonExistingAuthor.getFirstName(), nonExistingAuthor.getLastName());
        when(authorService.update(nonExistingAuthor.getId(), requestDto))
        .thenThrow(EntityNotFoundException.class);
        assertThatThrownBy(() ->
                mvc.perform(put("/author/" + nonExistingAuthor.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto))))
                .matches(e -> e.getCause() instanceof EntityNotFoundException);
    }
}
