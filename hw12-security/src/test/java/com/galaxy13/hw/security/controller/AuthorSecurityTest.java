package com.galaxy13.hw.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galaxy13.hw.controller.AuthorController;
import com.galaxy13.hw.dto.upsert.AuthorUpsertDto;
import com.galaxy13.hw.exception.controller.GlobalExceptionHandler;
import com.galaxy13.hw.security.config.BookStorageSecurityConfig;
import com.galaxy13.hw.service.AuthorService;
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

@DisplayName("Author Controller Security Test")
@WebMvcTest(AuthorController.class)
@Import({BookStorageSecurityConfig.class, GlobalExceptionHandler.class})
public class AuthorSecurityTest {
    private static final String AUTHORS_PATH = "/api/v1/author";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthorService authorService;

    @Nested
    @DisplayName("As User Role")
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    class UserRoleTests {

        @Test
        void canGetAllAuthors() throws Exception {
            mvc.perform(get(AUTHORS_PATH)).andExpect(status().isOk());
        }

        @Test
        void canGetOneAuthor() throws Exception {
            mvc.perform(get(AUTHORS_PATH + "/1")).andExpect(status().isOk());
        }

        @Test
        void cannotCreateAuthor() throws Exception {
            var createAuthorDto = new AuthorUpsertDto(0, "name", "surname");
            mvc.perform(post(AUTHORS_PATH)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createAuthorDto)))
                    .andExpect(status().isForbidden());
        }

        @Test
        void cannotUpdateAuthor() throws Exception {
            var updateAuthorDto = new AuthorUpsertDto(1, "name", "surname");
            mvc.perform(put(AUTHORS_PATH + "/1")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateAuthorDto)))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("As Admin Role")
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    class AdminRoleTests {

        @Test
        void canGetAllAuthors() throws Exception {
            mvc.perform(get(AUTHORS_PATH)).andExpect(status().isOk());
        }

        @Test
        void canGetOneAuthor() throws Exception {
            mvc.perform(get(AUTHORS_PATH + "/1")).andExpect(status().isOk());
        }

        @Test
        void canCreateAuthor() throws Exception {
            var createAuthorDto = new AuthorUpsertDto(0, "name", "surname");
            mvc.perform(post(AUTHORS_PATH)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createAuthorDto)))
                    .andExpect(status().isCreated());
        }

        @Test
        void canUpdateAuthor() throws Exception {
            var updateAuthorDto = new AuthorUpsertDto(1, "name", "surname");
            mvc.perform(put(AUTHORS_PATH + "/1")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateAuthorDto)))
                    .andExpect(status().isOk());
        }
    }
}
