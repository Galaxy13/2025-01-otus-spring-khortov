package com.galaxy13.hw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galaxy13.hw.dto.request.CommentRequestDto;
import com.galaxy13.hw.dto.response.CommentResponseDto;
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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        List<CommentResponseDto> expectedComments = bookIdToCommentMap().get(1L);
        when(commentService.findCommentByBookId(1L)).thenReturn(expectedComments);

        mvc.perform(get("/api/v1/comment?book_id=" + 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedComments)));
    }

    @Test
    void shouldReturnCommentById() throws Exception {
        CommentResponseDto expectedComment = getComments().getFirst();
        when(commentService.findCommentById(expectedComment.getId())).thenReturn(expectedComment);

        String uri = "/api/v1/comment/" + expectedComment.getId();
        mvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedComment)))
                .andReturn();
    }

    @Test
    void shouldThrowExceptionWhenCommentNotFound() {
        CommentResponseDto nonExistingComment = new CommentResponseDto(0, null, 0);
        when(commentService.findCommentById(nonExistingComment.getId())).thenThrow(EntityNotFoundException.class);

        assertThatThrownBy(() -> mvc.perform(get("/api/v1/comment/" + nonExistingComment.getId())))
                .matches(e -> e.getCause() instanceof EntityNotFoundException);
    }

    @Test
    void shouldEditComment() throws Exception {
        CommentResponseDto comment = getComments().getFirst();
        CommentResponseDto expectedComment = new CommentResponseDto(comment.getId(),
                "New text",
                comment.getBookId());
        CommentRequestDto requestDto = new CommentRequestDto(expectedComment.getText(), expectedComment.getBookId());
        when(commentService.update(expectedComment.getId(), requestDto)).thenReturn(expectedComment);

        String uri = "/api/v1/comment/" + comment.getId();

        mvc.perform(put(uri).contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedComment)));

        verify(commentService, times(1))
                .update(expectedComment.getId(), requestDto);
    }

    @Test
    void shouldCreateComment() throws Exception {
        CommentRequestDto requestDto = new CommentRequestDto("New text", 3);
        CommentResponseDto expected = new CommentResponseDto(8, "New text", 3);
        when(commentService.create(requestDto)).thenReturn(expected);

        String uri = "/api/v1/comment";
        mvc.perform(post(uri).contentType("application/json").content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
        verify(commentService, times(1)).create(requestDto);
    }
}
