package com.galaxy13.hw.batch.config;

import com.galaxy13.hw.batch.processor.GenreItemProcessor;
import com.galaxy13.hw.batch.reader.GenreItemReader;
import com.galaxy13.hw.batch.writer.GenreItemWriter;
import com.galaxy13.hw.model.mongo.MongoGenre;
import com.galaxy13.hw.model.jpa.JpaGenre;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class GenreStepMigrationConfig {

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
}
