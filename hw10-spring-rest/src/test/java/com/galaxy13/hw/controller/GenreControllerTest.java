package com.galaxy13.hw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galaxy13.hw.dto.request.GenreRequestDto;
import com.galaxy13.hw.dto.response.GenreResponseDto;
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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        List<GenreResponseDto> expectedGenres = getGenres();
        when(genreService.findAllGenres()).thenReturn(expectedGenres);

        mvc.perform(get("/api/v1/genre"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedGenres)));
    }

    @Test
    void shouldReturnGenreById() throws Exception {
        GenreResponseDto expectedGenre = getGenres().getFirst();
        GenreRequestDto requestDto = new GenreRequestDto(expectedGenre.getName());
        when(genreService.findGenreById(expectedGenre.getId())).thenReturn(expectedGenre);

        mvc.perform(get("/api/v1/genre/" + expectedGenre.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedGenre)));
    }

    @Test
    void shouldThrowExceptionWhenGenreNotFound() {
        GenreResponseDto nonExistingGenre = new GenreResponseDto(0, null);
        when(genreService.findGenreById(nonExistingGenre.getId())).thenThrow(EntityNotFoundException.class);

        assertThatThrownBy(() -> mvc.perform(get("/api/v1/genre/" + nonExistingGenre.getId())))
                .matches(e -> e.getCause() instanceof EntityNotFoundException);
    }

    @Test
    void shouldEditGenre() throws Exception {
        GenreResponseDto genre = getGenres().get(1);
        GenreResponseDto expectedGenre = new GenreResponseDto(genre.getId(), "New Genre");
        GenreRequestDto requestDto = new GenreRequestDto(genre.getName());
        when(genreService.update(expectedGenre.getId(), requestDto)).thenReturn(expectedGenre);

        String uri = "/api/v1/genre/" + genre.getId();

        mvc.perform(put(uri).contentType("application/json").content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedGenre)));
        verify(genreService, times(1))
                .update(expectedGenre.getId(), requestDto);
    }

    @Test
    void shouldCreateGenre() throws Exception {
        GenreResponseDto newGenre = new GenreResponseDto(7, "New Genre");
        GenreRequestDto requestDto = new GenreRequestDto(newGenre.getName());
        when(genreService.insert(requestDto)).thenReturn(newGenre);

        mvc.perform(post("/api/v1/genre")
                        .contentType("application/json").content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(newGenre)));
        verify(genreService, times(1)).insert(requestDto);
    }
}
