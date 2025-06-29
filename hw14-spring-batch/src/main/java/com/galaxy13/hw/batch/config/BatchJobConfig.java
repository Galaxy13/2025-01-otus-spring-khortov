package com.galaxy13.hw.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchJobConfig {

    @Bean
    public Job migrateJob(JobRepository jobRepository,
                          Step migrateBooksStep, Step migrateCommentsStep,
                          Flow authorGenreFlow) {

        Flow mainFlow = new FlowBuilder<Flow>("mainFlow")
                .start(authorGenreFlow)
                .next(migrateBooksStep)
                .next(migrateCommentsStep)
                .build();

        return new JobBuilder("migrateDataJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(mainFlow)
                .end().build();
    }
}
