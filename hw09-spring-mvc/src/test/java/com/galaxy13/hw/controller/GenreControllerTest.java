package com.galaxy13.hw.controller;

import com.galaxy13.hw.dto.GenreDto;
import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.helper.UriBuilder;
import com.galaxy13.hw.service.GenreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import static com.galaxy13.hw.helper.TestData.getGenres;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@WebMvcTest(controllers = GenreController.class)
class GenreControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private GenreService genreService;

    @Test
    void shouldReturnAllGenresView() throws Exception {
        List<GenreDto> expectedGenres = getGenres();
        when(genreService.findAllGenres()).thenReturn(expectedGenres);

        MvcResult result = mvc.perform(get("/genres"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("genres"))
                .andExpect(view().name("genres"))
                .andReturn();

        List<GenreDto> actualGenres = (List<GenreDto>) result.getModelAndView().getModel().get("genres");
        assertThat(actualGenres).usingRecursiveComparison().isEqualTo(expectedGenres);
    }

    @Test
    void shouldReturnEditGenreView() throws Exception {
        GenreDto expectedGenre = getGenres().getFirst();
        when(genreService.findGenreById(expectedGenre.getId())).thenReturn(Optional.of(expectedGenre));

        MvcResult result = mvc.perform(get("/genres/edit?id=" + expectedGenre.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("genre"))
                .andExpect(view().name("genre_edit"))
                .andReturn();

        GenreDto actualGenre = (GenreDto) result.getModelAndView().getModel().get("genre");
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @Test
    void shouldThrowExceptionWhenGenreNotFound() {
        GenreDto nonExistingGenre = new GenreDto(0, null);
        when(genreService.findGenreById(nonExistingGenre.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> mvc.perform(get("/genres/edit?id=" + nonExistingGenre.getId())))
                .matches(e -> e.getCause() instanceof EntityNotFoundException);
    }

    @Test
    void shouldEditGenre() throws Exception {
        GenreDto genre = getGenres().get(1);
        GenreDto expectedGenre = new GenreDto(genre.getId(), "New Genre");

        String uri = UriBuilder.fromStringUri("/genres/edit")
                .queryParam("id", expectedGenre.getId())
                .queryParam("name", expectedGenre.getName())
                .build();

        mvc.perform(post(uri))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/genres"));
        verify(genreService, times(1)).saveGenre(expectedGenre.getId(), expectedGenre.getName());
    }

    @Test
    void shouldCreateGenre() throws Exception {
        GenreDto newGenre = new GenreDto(0, "New Genre");

        String uri = "/genres/new?name=" + newGenre.getName();
        mvc.perform(post(uri))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/genres"));
        verify(genreService, times(1)).saveGenre(newGenre.getId(), newGenre.getName());
    }
}
