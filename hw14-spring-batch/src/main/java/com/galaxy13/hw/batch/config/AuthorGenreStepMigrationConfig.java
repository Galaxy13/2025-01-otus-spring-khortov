package com.galaxy13.hw.batch.config;

import com.galaxy13.hw.batch.processor.AuthorItemProcessor;
import com.galaxy13.hw.batch.processor.GenreItemProcessor;
import com.galaxy13.hw.batch.reader.AuthorItemReader;
import com.galaxy13.hw.batch.reader.GenreItemReader;
import com.galaxy13.hw.batch.writer.AuthorItemWriter;
import com.galaxy13.hw.batch.writer.GenreItemWriter;
import com.galaxy13.hw.model.jpa.JpaGenre;
import com.galaxy13.hw.model.mongo.MongoAuthor;
import com.galaxy13.hw.model.jpa.JpaAuthor;
import com.galaxy13.hw.model.mongo.MongoGenre;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class AuthorGenreStepMigrationConfig {

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor("author-genre-task");
        executor.setConcurrencyLimit(16);
        return executor;
    }

    @Bean
    public Step migrateAuthorsStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                   AuthorItemReader authorItemReader, AuthorItemWriter authorItemWriter,
                                   AuthorItemProcessor authorItemProcessor) {
        return new StepBuilder("migrateAuthorsStep", jobRepository)
                .<MongoAuthor, JpaAuthor>chunk(5, transactionManager)
                .reader(authorItemReader)
                .writer(authorItemWriter)
                .processor(authorItemProcessor).build();
    }

    @Bean
    public Step migrateGenresStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                  GenreItemReader genreItemReader, GenreItemWriter genreItemWriter,
                                  GenreItemProcessor genreItemProcessor) {
        return new StepBuilder("migrateGenresStep", jobRepository)
                .<MongoGenre, JpaGenre>chunk(5, transactionManager)
                .reader(genreItemReader)
                .writer(genreItemWriter)
                .processor(genreItemProcessor).build();
    }

    @Bean
    public Flow authorGenreFlow(Step migrateAuthorsStep, Step migrateGenresStep) {
        return new FlowBuilder<Flow>("authorGenreFlow")
                .split(taskExecutor())
                .add(
                        new FlowBuilder<Flow>("authorFlow").start(migrateAuthorsStep).build(),
                        new FlowBuilder<Flow>("genreFlow").start(migrateGenresStep).build()
                )
                .build();
    }
}
