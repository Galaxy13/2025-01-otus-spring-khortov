package com.galaxy13.hw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galaxy13.hw.dto.GenreDto;
import com.galaxy13.hw.dto.upsert.GenreUpsertDto;
import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.service.GenreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.galaxy13.hw.helper.TestData.getGenres;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GenreController.class)
class GenreControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private GenreService genreService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllGenres() throws Exception {
        List<GenreDto> expectedGenres = getGenres();
        when(genreService.findAllGenres()).thenReturn(expectedGenres);

        mvc.perform(get("/api/v1/genre"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedGenres)));
    }

    @Test
    void shouldReturnGenreById() throws Exception {
        GenreDto expectedGenre = getGenres().getFirst();
        GenreUpsertDto requestDto = new GenreUpsertDto(expectedGenre.id(), expectedGenre.name());
        when(genreService.findGenreById(expectedGenre.id())).thenReturn(expectedGenre);

        mvc.perform(get("/api/v1/genre/" + expectedGenre.id())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedGenre)));
    }

    @Test
    void shouldThrowExceptionWhenGenreNotFound() throws Exception {
        GenreDto nonExistingGenre = new GenreDto(0, null);
        when(genreService.findGenreById(nonExistingGenre.id())).thenThrow(EntityNotFoundException.class);

        mvc.perform(get("/api/v1/genre/" + nonExistingGenre.id()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldEditGenre() throws Exception {
        GenreDto genre = getGenres().get(1);
        GenreDto expectedGenre = new GenreDto(genre.id(), "New Genre");
        GenreUpsertDto requestDto = new GenreUpsertDto(expectedGenre.id(), genre.name());
        when(genreService.update(requestDto)).thenReturn(expectedGenre);

        String uri = "/api/v1/genre/" + genre.id();

        mvc.perform(put(uri).contentType("application/json").content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedGenre)));
        verify(genreService, times(1))
                .update(requestDto);
    }

    @Test
    void shouldCreateGenre() throws Exception {
        GenreDto newGenre = new GenreDto(7, "New Genre");
        GenreUpsertDto requestDto = new GenreUpsertDto(newGenre.id(), newGenre.name());
        when(genreService.create(requestDto)).thenReturn(newGenre);

        mvc.perform(post("/api/v1/genre")
                        .contentType("application/json").content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(newGenre)));
        verify(genreService, times(1)).create(requestDto);
    }

    @Test
    void shouldReturnBadRequestOnValidationError() throws Exception {
        mvc.perform(post("/api/v1/genre").contentType("application/json")
                        .content("""
                        {
                        "name": "New Genre"
                        }"""))
                .andExpect(status().isBadRequest());
    }
}
