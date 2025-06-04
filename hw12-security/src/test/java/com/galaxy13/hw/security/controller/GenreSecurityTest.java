package com.galaxy13.hw.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galaxy13.hw.controller.GenreController;
import com.galaxy13.hw.dto.upsert.GenreUpsertDto;
import com.galaxy13.hw.exception.controller.GlobalExceptionHandler;
import com.galaxy13.hw.security.config.BookStorageSecurityConfig;
import com.galaxy13.hw.service.GenreService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

    @Nested
    @DisplayName("As User Role")
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    class UserRoleTests {

        @Test
        void canGetAllGenres() throws Exception {
            mvc.perform(get(GENRES_PATH))
                    .andExpect(status().isOk());
        }

        @Test
        void canGetGenreById() throws Exception {
            mvc.perform(get(GENRES_PATH + "/1"))
                    .andExpect(status().isOk());
        }

        @Test
        void cannotCreateGenre() throws Exception {
            var createGenreDto = new GenreUpsertDto(0L, "name");
            mvc.perform(post(GENRES_PATH)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createGenreDto)))
                    .andExpect(status().isForbidden());
        }

        @Test
        void cannotUpdateGenre() throws Exception {
            var updateGenreDto = new GenreUpsertDto(1L, "name");
            mvc.perform(put(GENRES_PATH + "/1")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateGenreDto)))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("As Admin Role")
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    class AdminRoleTests {

        @Test
        void canGetAllGenres() throws Exception {
            mvc.perform(get(GENRES_PATH))
                    .andExpect(status().isOk());
        }

        @Test
        void canGetGenreById() throws Exception {
            mvc.perform(get(GENRES_PATH + "/1"))
                    .andExpect(status().isOk());
        }

        @Test
        void canCreateGenre() throws Exception {
            var createGenreDto = new GenreUpsertDto(1L, "new name");
            mvc.perform(post(GENRES_PATH)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createGenreDto)))
                    .andExpect(status().isCreated());
        }

        @Test
        void canUpdateGenre() throws Exception {
            var updateGenreDto = new GenreUpsertDto(1L, "name upd");
            mvc.perform(put(GENRES_PATH + "/1")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateGenreDto)))
                    .andExpect(status().isOk());
        }
    }
}
