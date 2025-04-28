package com.galaxy13.hw.controller;

import com.galaxy13.hw.dto.mvc.AuthorModelDto;
import com.galaxy13.hw.dto.service.AuthorDto;
import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.helper.UriBuilder;
import com.galaxy13.hw.service.AuthorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static com.galaxy13.hw.helper.TestData.getAuthors;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Test
    void shouldReturnViewForAllAuthors() throws Exception {
        when(authorService.findAllAuthors()).thenReturn(getAuthors());

        MvcResult result = mvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(view().name("authors"))
                .andReturn();

        List<AuthorDto> authors = (List<AuthorDto>) result.getModelAndView().getModel().get("authors");
        assertThat(authors).usingRecursiveComparison().isEqualTo(getAuthors());
    }

    @Test
    void shouldReturnEditViewForAuthor() throws Exception {
        AuthorDto author = getAuthors().getFirst();
        AuthorDto expectedAuthor = new AuthorDto(author.getId(), "New Name", "New Surname");
        when(authorService.findAuthorById(author.getId())).thenReturn(expectedAuthor);

        MvcResult result = mvc.perform(get("/authors/" + expectedAuthor.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("author"))
                .andExpect(view().name("author_edit"))
                .andReturn();

        AuthorDto actualAuthor = (AuthorDto) result.getModelAndView().getModel().get("author");
        assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @Test
    void shouldEditAuthor() throws Exception {
        AuthorDto author = getAuthors().getFirst();
        AuthorDto expectedAuthor = new AuthorDto(author.getId(), "New Name", "New Surname");

        String uri = UriBuilder.fromStringUri("/authors/" + expectedAuthor.getId())
                        .queryParam("firstName", expectedAuthor.getFirstName())
                        .queryParam("lastName", expectedAuthor.getLastName()).build();

        mvc.perform(post(uri))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/authors"));

        verify(authorService, times(1))
                .update(expectedAuthor.getId(), new AuthorModelDto(expectedAuthor.getFirstName(),
                        expectedAuthor.getLastName()));
    }

    @Test
    void shouldSaveNewAuthor() throws Exception {
        AuthorDto author = new AuthorDto(0, "New Name", "New Surname");

        String uri = UriBuilder.fromStringUri("/authors")
                .queryParam("firstName", author.getFirstName())
                .queryParam("lastName", author.getLastName())
                .build();

        mvc.perform(post(uri))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/authors"));

        verify(authorService, times(1)).insert(
                new AuthorModelDto(author.getFirstName(), author.getLastName()));
    }

    @Test
    void shouldThrowExceptionOnEditNonExistingAuthor() {
        AuthorDto nonExistingAuthor = new AuthorDto(0, null ,null);
        when(authorService.findAuthorById(nonExistingAuthor.getId()))
        .thenThrow(EntityNotFoundException.class);
        assertThatThrownBy(() -> mvc.perform(get("/authors/" + nonExistingAuthor.getId())))
                .matches(e -> e.getCause() instanceof EntityNotFoundException);
    }
}
