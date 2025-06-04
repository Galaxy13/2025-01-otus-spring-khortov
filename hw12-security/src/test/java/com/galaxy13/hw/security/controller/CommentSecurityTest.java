package com.galaxy13.hw.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galaxy13.hw.controller.CommentController;
import com.galaxy13.hw.dto.CommentDto;
import com.galaxy13.hw.dto.upsert.CommentUpsertDto;
import com.galaxy13.hw.exception.controller.GlobalExceptionHandler;
import com.galaxy13.hw.security.config.BookStorageSecurityConfig;
import com.galaxy13.hw.service.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Comment Controller Security Test")
@WebMvcTest(controllers = {CommentController.class})
@Import({BookStorageSecurityConfig.class, GlobalExceptionHandler.class})
public class CommentSecurityTest {
    private static final String COMMENTS_PATH = "/api/v1/comment";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CommentService commentService;

    @Nested
    @DisplayName("As User Role")
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    class UserRoleTests {

        @Test
        void canGetCommentsByBook() throws Exception {
            mvc.perform(get(COMMENTS_PATH + "?book_id=1"))
                    .andExpect(status().isOk());
        }

        @Test
        void canGetCommentById() throws Exception {
            mvc.perform(get(COMMENTS_PATH + "/1"))
                    .andExpect(status().isOk());
        }

        @Test
        void canCreateComment() throws Exception {
            var createCommentDto = new CommentUpsertDto(0L, "comment", 1L);
            mvc.perform(post(COMMENTS_PATH)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createCommentDto)))
                    .andExpect(status().isCreated());
        }

        @Test
        void canUpdateOwnComment() throws Exception {
            var updateCommentDto = new CommentUpsertDto(1L, "name", 1L);
            when(commentService.update(updateCommentDto))
                    .thenReturn(new CommentDto(updateCommentDto.id(),
                            updateCommentDto.text(),
                            updateCommentDto.bookId(),
                            true));

            mvc.perform(put(COMMENTS_PATH + "/1")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateCommentDto)))
                    .andExpect(status().isOk());
        }

        @Test
        void cannotUpdateOthersComment() throws Exception {
            var wrongCommentDto = new CommentUpsertDto(1L, "name", 1L);
            when(commentService.update(wrongCommentDto))
                    .thenThrow(new AccessDeniedException(""));

            mvc.perform(put(COMMENTS_PATH + "/1")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(wrongCommentDto)))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("As Admin Role")
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    class AdminRoleTests {

        @Test
        void canGetCommentsByBook() throws Exception {
            mvc.perform(get(COMMENTS_PATH + "?book_id=1"))
                    .andExpect(status().isOk());
        }

        @Test
        void canGetCommentById() throws Exception {
            mvc.perform(get(COMMENTS_PATH + "/1"))
                    .andExpect(status().isOk());
        }

        @Test
        void canCreateComment() throws Exception {
            var createCommentDto = new CommentUpsertDto(1L, "new comment", 1L);
            mvc.perform(post(COMMENTS_PATH)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createCommentDto)))
                    .andExpect(status().isCreated());
        }

        @Test
        void canUpdateAnyComment() throws Exception {
            var updateCommentDto = new CommentUpsertDto(1L, "name upd", 1L);
            mvc.perform(put(COMMENTS_PATH + "/1")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateCommentDto)))
                    .andExpect(status().isOk());
        }
    }
}
