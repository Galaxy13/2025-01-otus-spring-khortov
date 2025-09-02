package com.galaxy13.hw.health;

import com.galaxy13.hw.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"spring.sql.init.mode=never",
                "management.endpoints.web.exposure.include=health",
                "management.endpoint.health.show-details=always"}
)
@AutoConfigureMockMvc
class BookIndicatorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookRepository bookRepository;

    @Test
    void whenRepositoryIsNotEmptyIndicatorIsUp() throws Exception {
        when(bookRepository.count()).thenReturn(1L);

        mockMvc.perform(get("/actuator/health/book"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void whenRepositoryIsEmptyBookIndicatorIsDown() throws Exception {
        when(bookRepository.count()).thenReturn(0L);

        mockMvc.perform(get("/actuator/health/book"))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.status").value("DOWN"));
    }
}
