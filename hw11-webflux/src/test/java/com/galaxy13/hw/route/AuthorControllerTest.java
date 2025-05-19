package com.galaxy13.hw.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galaxy13.hw.dto.AuthorDto;
import com.galaxy13.hw.dto.upsert.AuthorUpsertDto;
import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.route.config.AuthorRouteConfig;
import com.galaxy13.hw.route.handler.AuthorHandler;
import com.galaxy13.hw.route.validator.UpsertDtoValidator;
import com.galaxy13.hw.service.AuthorService;
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

import static com.galaxy13.hw.helper.TestData.getAuthors;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@WebFluxTest(controllers = AuthorRouteConfig.class)
@Import({AuthorHandler.class, UpsertDtoValidator.class})
@ComponentScan("com.galaxy13.hw.exception.handler")
class AuthorControllerTest {

    @MockitoBean
    private AuthorService authorService;

    @Autowired
    private WebTestClient wtc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllAuthors() throws Exception {
        when(authorService.findAllAuthors()).thenReturn(Flux.fromStream(getAuthors().stream()));

        wtc.get().uri("/flux/author").accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json(objectMapper.writeValueAsString(getAuthors()));
    }

    @Test
    void shouldReturnAuthorById() throws Exception {
        AuthorDto author = getAuthors().getFirst();
        AuthorDto expectedAuthor = new AuthorDto(author.id(), "New Name", "New Surname");
        when(authorService.findAuthorById(author.id())).thenReturn(Mono.just(expectedAuthor));

        wtc.get().uri("/flux/author/" + expectedAuthor.id()).accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json(objectMapper.writeValueAsString(expectedAuthor));
    }

    @Test
    void shouldEditAuthor() throws Exception {
        AuthorDto author = getAuthors().getFirst();
        AuthorDto expectedAuthor = new AuthorDto(author.id(),
                "New Name", "New Surname");
        AuthorUpsertDto requestDto =
                new AuthorUpsertDto(expectedAuthor.id(), expectedAuthor.firstName(), expectedAuthor.lastName());
        when(authorService.update(requestDto)).thenReturn(Mono.just(expectedAuthor));

        wtc.put().uri("/flux/author/" + expectedAuthor.id())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(objectMapper.writeValueAsString(requestDto))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json(objectMapper.writeValueAsString(expectedAuthor));

        verify(authorService, times(1))
                .update(requestDto);
    }

    @Test
    void shouldSaveNewAuthor() throws Exception {
        AuthorDto expectedAuthor = new AuthorDto("4", "New Name", "New Surname");
        AuthorUpsertDto requestDto = new AuthorUpsertDto(expectedAuthor.id(),
                "New Name",
                "New Surname");
        when(authorService.create(requestDto)).thenReturn(Mono.just(expectedAuthor));

        String uri = "/flux/author";

        wtc.post().uri(uri).accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(objectMapper.writeValueAsString(requestDto))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.firstName").isEqualTo(expectedAuthor.firstName())
                .jsonPath("$.lastName").isEqualTo(expectedAuthor.lastName());
        verify(authorService, times(1)).create(requestDto);
    }

    @Test
    void shouldReturnNotFoundOnEditNonExistingAuthor() throws Exception {
        AuthorDto nonExistingAuthor = new AuthorDto("0", "Phillip" ,"Dick");
        AuthorUpsertDto requestDto =
                new AuthorUpsertDto(nonExistingAuthor.id(),
                        nonExistingAuthor.firstName(), nonExistingAuthor.lastName());
        when(authorService.update(requestDto)).thenReturn(Mono.error(new EntityNotFoundException("")));
        wtc.put().uri("/flux/author/" + nonExistingAuthor.id())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(requestDto))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void shouldReturnBadRequestOnValidationError() {
        wtc.post().uri("/flux/author").contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                        "id": 0,
                        "lastName": "Marx"
                        }""")
                .exchange().expectStatus().isBadRequest();
    }
}
