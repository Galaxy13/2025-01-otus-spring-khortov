package com.galaxy13.hw.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galaxy13.hw.controller.CommentController;
import com.galaxy13.hw.dto.CommentDto;
import com.galaxy13.hw.dto.upsert.CommentUpsertDto;
import com.galaxy13.hw.exception.controller.GlobalExceptionHandler;
import com.galaxy13.hw.security.config.BookStorageSecurityConfig;
import com.galaxy13.hw.service.CommentService;
import org.junit.jupiter.api.DisplayName;
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
@WebMvcTest(controllers = {CommentController.class,})
@Import({BookStorageSecurityConfig.class, GlobalExceptionHandler.class,})
public class CommentSecurityTest {
    private static final String COMMENTS_PATH = "/api/v1/comment";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CommentService commentService;

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    void testAuthenticatedOnUser() throws Exception {
        // Comments by book
        mvc.perform(get(COMMENTS_PATH + "?book_id=1")).andExpect(status().isOk());
        // Comment by id
        mvc.perform(get(COMMENTS_PATH + "/1")).andExpect(status().isOk());
        // Create (POST)
        var creatCommentDto = new CommentUpsertDto(0L, "comment", 1L);
        mvc.perform(post(COMMENTS_PATH).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(creatCommentDto))).andExpect(status().isCreated());
        // Update (PUT) for user who created
        var updateCommentDto = new CommentUpsertDto(1L, "name", 1L);
        when(commentService.update(updateCommentDto))
                .thenReturn(new CommentDto(updateCommentDto.id(),
                        updateCommentDto.text(), updateCommentDto.bookId(),true));
        mvc.perform(put(COMMENTS_PATH + "/1").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCommentDto))).andExpect(status().isOk());

        // Update (PUT) for other user
        var wrongCommentDto = new CommentUpsertDto(1L, "name", 1L);
        when(commentService.update(wrongCommentDto)).thenThrow(new AccessDeniedException(""));
        mvc.perform(put(COMMENTS_PATH + "/1").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wrongCommentDto))).andExpect(status().isForbidden());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    void testAuthenticatedOnAdmin() throws Exception {
        // All comment
        mvc.perform(get(COMMENTS_PATH + "?book_id=1"))
                .andExpect(status().isOk());

        // One comment
        mvc.perform(get(COMMENTS_PATH + "/1"))
                .andExpect(status().isOk());

        // Create (POST)
        var createCommentDto = new CommentUpsertDto(1L, "new comment", 1L);
        mvc.perform(post(COMMENTS_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCommentDto))
        ).andExpect(status().isCreated());

        // Update (PUT) allowed for all comments
        var updateCommentDtop = new CommentUpsertDto(1L, "name upd", 1L);
        mvc.perform(put(COMMENTS_PATH + "/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCommentDtop))
        ).andExpect(status().isOk());
    }
}
