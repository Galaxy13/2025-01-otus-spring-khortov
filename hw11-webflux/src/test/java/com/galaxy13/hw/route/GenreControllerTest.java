package com.galaxy13.hw.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galaxy13.hw.dto.GenreDto;
import com.galaxy13.hw.dto.upsert.GenreUpsertDto;
import com.galaxy13.hw.exception.EntityNotFoundException;
import com.galaxy13.hw.exception.handler.GlobalExceptionHandler;
import com.galaxy13.hw.exception.handler.StorageErrorAttributes;
import com.galaxy13.hw.route.config.GenreRouteConfig;
import com.galaxy13.hw.route.handler.GenreHandler;
import com.galaxy13.hw.route.validator.UpsertDtoValidator;
import com.galaxy13.hw.service.GenreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.galaxy13.hw.helper.TestData.getGenres;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@WebFluxTest(controllers = GenreRouteConfig.class)
@Import({GlobalExceptionHandler.class,
        GenreHandler.class, StorageErrorAttributes.class, UpsertDtoValidator.class})
class GenreControllerTest {

    @Autowired
    private WebTestClient wtc;

    @MockitoBean
    private GenreService genreService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllGenres() throws Exception {
        List<GenreDto> expectedGenres = getGenres();
        when(genreService.findAllGenres()).thenReturn(Flux.fromStream(expectedGenres.stream()));

        wtc.get().uri("/flux/genre")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json(objectMapper.writeValueAsString(expectedGenres));
    }

    @Test
    void shouldReturnGenreById() throws Exception {
        GenreDto expectedGenre = getGenres().getFirst();
        when(genreService.findGenreById(expectedGenre.id())).thenReturn(Mono.just(expectedGenre));

        wtc.get().uri("/flux/genre/" + expectedGenre.id())
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(objectMapper.writeValueAsString(expectedGenre));
    }

    @Test
    void shouldReturnNotFoundWhenGenreNotFound() {
        GenreDto nonExistingGenre = new GenreDto("0", null);
        when(genreService.findGenreById(nonExistingGenre.id())).thenReturn(Mono.error(new EntityNotFoundException("")));

        wtc.get().uri("/flux/genre/" + nonExistingGenre.id())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void shouldEditGenre() throws Exception {
        GenreDto genre = getGenres().get(1);
        GenreDto expectedGenre = new GenreDto(genre.id(), "New Genre");
        GenreUpsertDto requestDto = new GenreUpsertDto(expectedGenre.id(), genre.name());
        when(genreService.update(requestDto)).thenReturn(Mono.just(expectedGenre));

        String uri = "/flux/genre/" + genre.id();

        wtc.put().uri(uri).contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(requestDto))
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(objectMapper.writeValueAsString(expectedGenre));

        verify(genreService, times(1))
                .update(requestDto);
    }

    @Test
    void shouldCreateGenre() throws Exception {
        GenreDto newGenre = new GenreDto("0", "New Genre");
        GenreUpsertDto requestDto = new GenreUpsertDto(newGenre.id(), newGenre.name());
        when(genreService.create(requestDto)).thenReturn(Mono.just(newGenre));

        wtc.post().uri("/flux/genre").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(requestDto))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                        .jsonPath("$.name").isEqualTo(newGenre.name());

        verify(genreService, times(1)).create(requestDto);
    }

    @Test
    void shouldReturnBadRequestOnValidationError() {
        wtc.post().uri("/flux/genre").contentType(MediaType.APPLICATION_JSON)
                        .bodyValue("""
                        {
                        "name": "New Genre"
                        }""")
                .exchange()
                .expectStatus().isBadRequest();
    }
}
