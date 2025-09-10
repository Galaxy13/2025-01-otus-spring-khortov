package com.galaxy13.hw.security.service;

import com.galaxy13.hw.dto.upsert.CommentUpsertDto;
import com.galaxy13.hw.model.Book;
import com.galaxy13.hw.model.Comment;
import com.galaxy13.hw.repository.BookRepository;
import com.galaxy13.hw.repository.CommentRepository;
import com.galaxy13.hw.security.model.Role;
import com.galaxy13.hw.security.model.User;
import com.galaxy13.hw.service.CommentService;
import com.galaxy13.hw.service.CommentServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@DisplayName("Comment Service Security Test")
@SpringBootTest(classes = {CommentServiceImpl.class})
@EnableAutoConfiguration(exclude =
        {SecurityAutoConfiguration.class, HibernateJpaAutoConfiguration.class,
                DataSourceAutoConfiguration.class,
                JpaRepositoriesAutoConfiguration.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ComponentScan("com.galaxy13.hw.mapper")
public class CommentServiceSecurityTest {

    private static Authentication auth;

    @Autowired
    private CommentService commentService;

    @MockitoBean
    private CommentRepository commentRepository;

    @MockitoBean
    private BookRepository bookRepository;

    @Nested
    @DisplayName("Test with user")
    class UpdateCommentUserTests {

        @BeforeAll
        static void setUser() {
            UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000001");
            CustomUserDetails principal = new CustomUserDetails(new User(userId,
                    "user", "password", Role.USER));
            auth = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        }

        @BeforeEach
        void setUp() {
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        @AfterEach
        void tearDown() {
            SecurityContextHolder.clearContext();
        }

        @Test
        void testUpdateWithOwnerUser() {
            UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000001");
            CommentUpsertDto dto = new CommentUpsertDto(1L, "Updated text", 1L);
            Comment comment = new Comment(1L, "Updated text", userId.toString(), new Book());
            when(commentRepository.findById(dto.id())).thenReturn(Optional.of(comment));
            commentService.update(dto);
        }

        @Test
        void testUpdateWithNotOwnerUser() {
            CommentUpsertDto newDto = new CommentUpsertDto(1L, "Updated text", 1L);
            Comment otherComment = new Comment(1L, "Updated text", "00000000-0000-0000-0000-000000000002", new Book());
            when(commentRepository.findById(newDto.id())).thenReturn(Optional.of(otherComment));
            assertThatThrownBy(() -> commentService.update(newDto)).isInstanceOf(AccessDeniedException.class);
        }
    }


    @Nested
    @DisplayName("Test with admin")
    class UpdateCommentAdminTests {

        @BeforeAll
        static void setUser() {
            UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000003");
            CustomUserDetails principal = new CustomUserDetails(new User(userId,
                    "admin", "password", Role.ADMIN));
            auth = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        }

        @BeforeEach
        void setUp() {
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        @AfterEach
        void tearDown() {
            SecurityContextHolder.clearContext();
        }

        @Test
        void testUpdateWithOwnerUserAdmin() {
            UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000003");
            CommentUpsertDto dto = new CommentUpsertDto(1L, "Updated text", 1L);
            Comment comment = new Comment(1L, "Updated text", userId.toString(), new Book());
            when(commentRepository.findById(dto.id())).thenReturn(Optional.of(comment));
            commentService.update(dto);
        }

        @Test
        void testUpdateWithNotOwnerUserAdmin() {
            CommentUpsertDto newDto = new CommentUpsertDto(1L, "Updated text", 1L);
            Comment otherComment = new Comment(1L, "Updated text", "00000000-0000-0000-0000-000000000001", new Book());
            when(commentRepository.findById(newDto.id())).thenReturn(Optional.of(otherComment));
            commentService.update(newDto);
        }
    }

}
