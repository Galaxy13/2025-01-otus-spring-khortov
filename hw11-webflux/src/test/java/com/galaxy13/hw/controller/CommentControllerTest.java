package com.galaxy13.hw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galaxy13.hw.dto.CommentDto;
import com.galaxy13.hw.dto.upsert.CommentUpsertDto;
import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.galaxy13.hw.helper.TestData.bookIdToCommentMap;
import static com.galaxy13.hw.helper.TestData.getComments;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CommentService commentService;

    @Test
    void shouldReturnCommentsForBook() throws Exception {
        List<CommentDto> expectedComments = bookIdToCommentMap().get(1L);
        when(commentService.findCommentByBookId(1L)).thenReturn(expectedComments);

        mvc.perform(get("/api/v1/comment?book_id=" + 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedComments)));
    }

    @Test
    void shouldReturnCommentById() throws Exception {
        CommentDto expectedComment = getComments().getFirst();
        when(commentService.findCommentById(expectedComment.id())).thenReturn(expectedComment);

        String uri = "/api/v1/comment/" + expectedComment.id();
        mvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedComment)))
                .andReturn();
    }

    @Test
    void shouldThrowExceptionWhenCommentNotFound() throws Exception {
        CommentDto nonExistingComment = new CommentDto(0, null, 0);
        when(commentService.findCommentById(nonExistingComment.id())).thenThrow(EntityNotFoundException.class);

        mvc.perform(get("/api/v1/comment/" + nonExistingComment.id()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldEditComment() throws Exception {
        CommentDto comment = getComments().getFirst();
        CommentDto expectedComment = new CommentDto(comment.id(),
                "New text",
                comment.bookId());
        CommentUpsertDto requestDto = new CommentUpsertDto(expectedComment.id(),
                expectedComment.text(), expectedComment.bookId());
        when(commentService.update(requestDto)).thenReturn(expectedComment);

        String uri = "/api/v1/comment/" + comment.id();

        mvc.perform(put(uri).contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedComment)));

        verify(commentService, times(1))
                .update(requestDto);
    }

    @Test
    void shouldCreateComment() throws Exception {
        CommentUpsertDto requestDto = new CommentUpsertDto(0L, "New text", 3L);
        CommentDto expected = new CommentDto(8, "New text", 3);
        when(commentService.create(requestDto)).thenReturn(expected);

        String uri = "/api/v1/comment";
        mvc.perform(post(uri).contentType("application/json").content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
        verify(commentService, times(1)).create(requestDto);
    }

    @Test
    void shouldReturnBadRequestOnValidationError() throws Exception {
        mvc.perform(post("/api/v1/comment").contentType("application/json")
                        .content("""
                        {
                        "id": 0,
                        "text": "New Comment"
                        }"""))
                .andExpect(status().isBadRequest());
    }
}
