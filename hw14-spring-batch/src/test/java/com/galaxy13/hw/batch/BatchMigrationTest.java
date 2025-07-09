package com.galaxy13.hw.batch;

import com.galaxy13.hw.AbstractBaseBatchTest;
import com.galaxy13.hw.HelperData;
import com.galaxy13.hw.model.mongo.MongoAuthor;
import com.galaxy13.hw.model.mongo.MongoGenre;
import com.galaxy13.hw.model.jpa.JpaAuthor;
import com.galaxy13.hw.model.jpa.JpaBook;
import com.galaxy13.hw.model.jpa.JpaComment;
import com.galaxy13.hw.model.jpa.JpaGenre;
import com.galaxy13.hw.repository.jpa.JpaAuthorRepository;
import com.galaxy13.hw.repository.jpa.JpaBookRepository;
import com.galaxy13.hw.repository.jpa.JpaCommentRepository;
import com.galaxy13.hw.repository.jpa.JpaGenreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBatchTest
@SpringBootTest
@Sql(scripts = {"/cleanup.sql",},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class BatchMigrationTest extends AbstractBaseBatchTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JpaAuthorRepository authorRepository;

    @Autowired
    private JpaBookRepository bookRepository;

    @Autowired
    private JpaGenreRepository genreRepository;

    @Autowired
    private JpaCommentRepository commentRepository;

    @Test
    void testBatchMigration() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
                .toJobParameters();
        JobExecution jobExecution =  jobLauncherTestUtils.launchJob(jobParameters);
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

        testAuthors();

        testGenres();

        testBooks();

        testComments();
    }

    @Test
    void testBatchMigrationWithNoBooks() throws Exception {
        MongoTemplate mongoTemplate = getMongoTemplate();
        mongoTemplate.getDb().drop();

        MongoAuthor author = new MongoAuthor("mongo", "author");
        MongoGenre genre = new MongoGenre("mongo", "genre");

        mongoTemplate.insert(author, "authors");
        mongoTemplate.insert(genre, "genres");
        JpaAuthor expectedAuthor = new JpaAuthor(0L, author.getFirstName(), author.getLastName());
        JpaGenre expectedGenre = new JpaGenre(0L, genre.getName());

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis()).toJobParameters();
        JobExecution jobExecution =  jobLauncherTestUtils.launchJob(jobParameters);
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

        List<JpaAuthor> actualAuthors = authorRepository.findAll();
        assertThat(actualAuthors).hasSize(1).usingRecursiveComparison().ignoringFields("id")
                .isEqualTo(List.of(expectedAuthor));

        List<JpaGenre> actualGenres = genreRepository.findAll();
        assertThat(actualGenres).hasSize(1).usingRecursiveComparison().ignoringFields("id")
                .isEqualTo(List.of(expectedGenre));
    }

    private void testAuthors() {
        List<JpaAuthor> actualAuthors = authorRepository.findAll();
        List<JpaAuthor> expectedAuthors = HelperData.getAuthors();
        assertThat(actualAuthors).usingRecursiveComparison().isEqualTo(expectedAuthors);
    }

    private void testGenres() {
        List<JpaGenre> actualGenres = genreRepository.findAll();
        List<JpaGenre> expectedGenres = HelperData.getGenres();
        assertThat(actualGenres).usingRecursiveComparison().isEqualTo(expectedGenres);
    }

    private void testBooks() {
        List<JpaBook> actualBooks = bookRepository.findAll();
        List<JpaBook> expectedBooks = HelperData.getBooks();
        assertThat(actualBooks).usingRecursiveComparison().isEqualTo(expectedBooks);
    }

    private void testComments() {
        List<JpaComment> actualComments = commentRepository.findAll();
        List<JpaComment> expectedComments = HelperData.getComments();
        Predicate<JpaComment> matchComments = actualComment ->
                expectedComments.stream().anyMatch(expectedComment ->
                        actualComment.getText().equals(expectedComment.getText()) &&
                                actualComment.getBook().getTitle().equals(expectedComment.getBook().getTitle()));
        assertThat(actualComments).isNotEmpty().allMatch(matchComments);
    }
}
