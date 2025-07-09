package com.galaxy13.hw.batch.config;

import com.galaxy13.hw.batch.processor.CommentItemProcessor;
import com.galaxy13.hw.batch.reader.CommentItemReader;
import com.galaxy13.hw.batch.writer.CommentItemWriter;
import com.galaxy13.hw.model.mongo.MongoComment;
import com.galaxy13.hw.model.jpa.JpaComment;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class CommentStepMigrationConfig {

    @Bean
    public Step migrateCommentsStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                   CommentItemReader commentItemReader, CommentItemWriter commentItemWriter,
                                   CommentItemProcessor commentItemProcessor) {
        return new StepBuilder("migrateCommentsStep", jobRepository)
                .<MongoComment, JpaComment>chunk(5, transactionManager)
                .reader(commentItemReader)
                .writer(commentItemWriter)
                .processor(commentItemProcessor).build();
    }
}
