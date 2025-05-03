package com.galaxy13.hw.controller;

import com.galaxy13.hw.dto.service.CommentDto;
import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.helper.UriBuilder;
import com.galaxy13.hw.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import static com.galaxy13.hw.helper.TestData.bookIdToCommentMap;
import static com.galaxy13.hw.helper.TestData.getComments;
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

@WebMvcTest(controllers = CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CommentService commentService;

    @Test
    void shouldReturnCommentsForBookView() throws Exception {
        List<CommentDto> expectedComments = bookIdToCommentMap().get(1L);
        when(commentService.findCommentByBookId(1L)).thenReturn(expectedComments);

        MvcResult result = mvc.perform(get("/comments?book_id=" + 1L))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("comments"))
                .andExpect(view().name("book_comments"))
                .andReturn();

        List<CommentDto> actualComments = (List<CommentDto>) result.getModelAndView().getModel().get("comments");
        assertThat(actualComments).usingRecursiveComparison().isEqualTo(expectedComments);
    }

    @Test
    void shouldReturnEditCommentView() throws Exception {
        CommentDto expectedComment = getComments().getFirst();
        when(commentService.findCommentById(expectedComment.getId())).thenReturn(Optional.of(expectedComment));

        String uri = "/comments/" + expectedComment.getId();
        MvcResult result = mvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("comment"))
                .andExpect(view().name("comment_edit"))
                .andReturn();

        CommentDto actualComment = (CommentDto) result.getModelAndView().getModel().get("comment");
        assertThat(actualComment).usingRecursiveComparison().isEqualTo(expectedComment);
    }

    @Test
    void shouldThrowExceptionWhenCommentNotFound() {
        CommentDto nonExistingComment = new CommentDto(0, null, 0);
        when(commentService.findCommentById(nonExistingComment.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> mvc.perform(get("/comments/" + nonExistingComment.getId())))
                .matches(e -> e.getCause() instanceof EntityNotFoundException);
    }

    @Test
    void shouldEditComment() throws Exception {
        CommentDto comment = getComments().getFirst();
        CommentDto expectedComment = new CommentDto(comment.getId(), "New text", comment.getBookId());

        String uri = UriBuilder.fromStringUri("/comments/" + expectedComment.getBookId())
                .queryParam("comment", expectedComment.getText())
                .queryParam("bookId", expectedComment.getBookId())
                .build();

        mvc.perform(post(uri))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/comments?book_id=" + expectedComment.getBookId()));

        verify(commentService, times(1))
                .update(expectedComment.getId(), expectedComment.getText());
    }

    @Test
    void shouldCreateComment() throws Exception {
        CommentDto newComment = new CommentDto(0, "New text", 3);

        String uri = UriBuilder.fromStringUri("/comments")
                .queryParam("comment", newComment.getText())
                .queryParam("bookId", newComment.getBookId())
                .build();
        mvc.perform(post(uri))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/comments?book_id=" + newComment.getBookId()));
        verify(commentService, times(1)).create(newComment.getText(), newComment.getBookId());
    }
}
