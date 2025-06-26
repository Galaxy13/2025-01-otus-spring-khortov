package com.galaxy13.hw.batch.config;

import com.galaxy13.hw.batch.processor.BookItemProcessor;
import com.galaxy13.hw.batch.reader.BookItemReader;
import com.galaxy13.hw.batch.writer.BookItemWriter;
import com.galaxy13.hw.model.mongo.MongoBook;
import com.galaxy13.hw.model.jpa.JpaBook;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BookStepMigrationConfig {

    @Bean
    public Step migrateBooksStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                 BookItemReader bookItemReader, BookItemWriter bookItemWriter,
                                 BookItemProcessor bookItemProcessor) {
        return new StepBuilder("migrateBooksStep", jobRepository)
                .<MongoBook, JpaBook>chunk(5, transactionManager)
                .reader(bookItemReader)
                .writer(bookItemWriter)
                .processor(bookItemProcessor).build();
    }
}
