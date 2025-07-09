package com.galaxy13.hw.batch.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@RequiredArgsConstructor
public class JobFinishHelper implements JobExecutionListener {

    private final DataSource dataSource;

    @Override
    public void afterJob(JobExecution jobExecution) {
        try {
            if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
                Resource resource = new ClassPathResource(
                        "org/springframework/batch/core/schema-drop-postgresql.sql");

                ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
                databasePopulator.addScript(resource);
                databasePopulator.setSeparator(";");
                databasePopulator.execute(dataSource);

            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to drop temporary tables", e);
        }
    }
}
