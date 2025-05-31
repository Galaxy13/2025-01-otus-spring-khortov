package com.galaxy13.hw.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galaxy13.hw.controller.GenreController;
import com.galaxy13.hw.dto.upsert.GenreUpsertDto;
import com.galaxy13.hw.exception.controller.GlobalExceptionHandler;
import com.galaxy13.hw.security.config.BookStorageSecurityConfig;
import com.galaxy13.hw.service.GenreService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Genre Controller Security Test")
@WebMvcTest(controllers = {GenreController.class})
@Import({BookStorageSecurityConfig.class, GlobalExceptionHandler.class})
public class GenreSecurityTest {
    private static final String GENRES_PATH = "/api/v1/genre";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GenreService genreService;

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    void testAuthenticatedOnUser() throws Exception {
        // All genres
        mvc.perform(get(GENRES_PATH))
                .andExpect(status().isOk());

        // One genre
        mvc.perform(get(GENRES_PATH + "/1"))
                .andExpect(status().isOk());

        // Create (POST)
        var createGenreDto = new GenreUpsertDto(0L, "name");
        mvc.perform(post(GENRES_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createGenreDto))
        ).andExpect(status().isForbidden());

        // Update (PUT)
        var updateGenreDto = new GenreUpsertDto(1L, "name");
        mvc.perform(put(GENRES_PATH + "/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateGenreDto))
        ).andExpect(status().isForbidden());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    void testAuthenticatedOnAdmin() throws Exception {
        // All genres
        mvc.perform(get(GENRES_PATH))
                .andExpect(status().isOk());

        // One genre
        mvc.perform(get(GENRES_PATH + "/1"))
                .andExpect(status().isOk());

        // Create (POST)
        var createGenreDto = new GenreUpsertDto(1L, "new name");
        mvc.perform(post(GENRES_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createGenreDto))
        ).andExpect(status().isCreated());

        // Update (PUT)
        var updateGenreDto = new GenreUpsertDto(1L, "name upd");
        mvc.perform(put(GENRES_PATH + "/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateGenreDto))
        ).andExpect(status().isOk());
    }
}
