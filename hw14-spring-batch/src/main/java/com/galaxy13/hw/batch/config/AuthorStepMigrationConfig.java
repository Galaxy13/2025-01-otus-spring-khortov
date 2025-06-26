package com.galaxy13.hw.batch.config;

import com.galaxy13.hw.batch.processor.AuthorItemProcessor;
import com.galaxy13.hw.batch.reader.AuthorItemReader;
import com.galaxy13.hw.batch.writer.AuthorItemWriter;
import com.galaxy13.hw.model.mongo.MongoAuthor;
import com.galaxy13.hw.model.jpa.JpaAuthor;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class AuthorStepMigrationConfig {

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
}
