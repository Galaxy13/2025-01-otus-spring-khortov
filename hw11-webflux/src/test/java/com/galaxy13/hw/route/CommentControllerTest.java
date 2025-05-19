package com.galaxy13.hw.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galaxy13.hw.dto.CommentDto;
import com.galaxy13.hw.dto.upsert.CommentUpsertDto;
import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.route.config.CommentRouteConfig;
import com.galaxy13.hw.route.handler.CommentHandler;
import com.galaxy13.hw.route.validator.UpsertDtoValidator;
import com.galaxy13.hw.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.galaxy13.hw.helper.TestData.bookIdToCommentMap;
import static com.galaxy13.hw.helper.TestData.getComments;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@WebFluxTest(controllers = CommentRouteConfig.class)
@Import({CommentHandler.class, UpsertDtoValidator.class})
@ComponentScan("com.galaxy13.hw.exception.handler")
class CommentControllerTest {

    @Autowired
    private WebTestClient wtc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CommentService commentService;

    @Test
    void shouldReturnCommentsForBook() throws Exception {
        String bookId = "1";
        List<CommentDto> expectedComments = bookIdToCommentMap().get(bookId);
        when(commentService.findCommentByBookId(bookId))
                .thenReturn(Flux.fromIterable(expectedComments));

        wtc.get().uri(uriBuilder ->
                        uriBuilder.path("/flux/comment").queryParam("book_id", bookId).build()).
                exchange()
                .expectStatus().isOk()
                .expectBody()
                .json(objectMapper.writeValueAsString(expectedComments));
    }

    @Test
    void shouldReturnCommentById() throws Exception {
        CommentDto expectedComment = getComments().getFirst();
        when(commentService.findCommentById(expectedComment.id())).thenReturn(Mono.just(expectedComment));

        String uri = "/flux/comment/" + expectedComment.id();
        wtc.get().uri(uri).exchange()
                .expectStatus().isOk()
                .expectBody()
                .json(objectMapper.writeValueAsString(expectedComment));
    }

    @Test
    void shouldThrowExceptionWhenCommentNotFound() {
        CommentDto nonExistingComment = new CommentDto("0", null, "0");
        when(commentService.findCommentById(nonExistingComment.id()))
                .thenReturn(Mono.error(new EntityNotFoundException("")));

        wtc.get().uri("/flux/comment/" + nonExistingComment.id()).exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void shouldEditComment() throws Exception {
        CommentDto comment = getComments().getFirst();
        CommentDto expectedComment = new CommentDto(comment.id(),
                "New text",
                comment.bookId());
        CommentUpsertDto requestDto = new CommentUpsertDto(expectedComment.id(),
                expectedComment.text(), expectedComment.bookId());
        when(commentService.update(requestDto)).thenReturn(Mono.just(expectedComment));

        String uri = "/flux/comment/" + comment.id();

        wtc.put().uri(uri).contentType(MediaType.APPLICATION_JSON).bodyValue(requestDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json(objectMapper.writeValueAsString(expectedComment));

        verify(commentService, times(1))
                .update(requestDto);
    }

    @Test
    void shouldCreateComment() {
        CommentUpsertDto requestDto = new CommentUpsertDto("0", "New text", "3");
        CommentDto expected = new CommentDto("", "New text", "3");
        when(commentService.create(requestDto)).thenReturn(Mono.just(expected));

        String uri = "/flux/comment";
        wtc.post().uri(uri).contentType(MediaType.APPLICATION_JSON).bodyValue(requestDto).exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.text").isEqualTo(expected.text())
                .jsonPath("$.bookId").isEqualTo(expected.bookId());

        verify(commentService, times(1)).create(requestDto);
    }

    @Test
    void shouldReturnBadRequestOnValidationError() {
        wtc.post().uri("/flux/comment").contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                        "id": 0,
                        "text": "New Comment"
                        }""")
                .exchange()
                .expectStatus().isBadRequest();
    }
}
