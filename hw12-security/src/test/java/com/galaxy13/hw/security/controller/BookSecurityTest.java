package com.galaxy13.hw.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galaxy13.hw.controller.BookController;
import com.galaxy13.hw.dto.upsert.BookUpsertDto;
import com.galaxy13.hw.exception.controller.GlobalExceptionHandler;
import com.galaxy13.hw.security.config.BookStorageSecurityConfig;
import com.galaxy13.hw.service.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@DisplayName("Book Controller Security Test")
@WebMvcTest(controllers = BookController.class)
@Import({BookStorageSecurityConfig.class, GlobalExceptionHandler.class,})
public class BookSecurityTest {
    private static final String BOOKS_PATH = "/api/v1/book";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookService bookService;

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    void testAuthenticatedOnUser() throws Exception {
        // All books
        mvc.perform(get(BOOKS_PATH)).andExpect(status().isOk());

        // One book
        mvc.perform(get(BOOKS_PATH + "/1")).andExpect(status().isOk());

        // Create (POST)
        var createBookDto = new BookUpsertDto(0L, "title", 1L, Set.of(1L));
        mvc.perform(post(BOOKS_PATH).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createBookDto))
        ).andExpect(status().isForbidden());

        // Update (PUT)
        var updateBookDto = new BookUpsertDto(1L, "new book", 1L, Set.of(1L));
        mvc.perform(put(BOOKS_PATH + "/1").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateBookDto))
        ).andExpect(status().isForbidden());

        // Delete
        mvc.perform(delete(BOOKS_PATH + "/1")).andExpect(status().isForbidden());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    void testAuthenticatedOnAdmin() throws Exception {
        // All books
        mvc.perform(get(BOOKS_PATH)).andExpect(status().isOk());

        // One book
        mvc.perform(get(BOOKS_PATH + "/1")).andExpect(status().isOk());

        // Create (POST)
        var createBookDto = new BookUpsertDto(0L, "new book", 1L, Set.of(1L));
        mvc.perform(post(BOOKS_PATH).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createBookDto))
        ).andExpect(status().isCreated());

        // Update (PUT)
        var updateBookDto = new BookUpsertDto(1L, "title upd", 1L, Set.of(1L));
        mvc.perform(put(BOOKS_PATH + "/1").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateBookDto))
        ).andExpect(status().isOk());

        // Delete
        mvc.perform(delete(BOOKS_PATH + "/1")).andExpect(status().isNoContent());
    }
}
