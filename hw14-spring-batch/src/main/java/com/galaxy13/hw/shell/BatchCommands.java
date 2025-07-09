package com.galaxy13.hw.shell;

import com.galaxy13.hw.batch.listener.JobFinishHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.shell.command.annotation.Command;

@Command(description = "Author Commands")
@RequiredArgsConstructor
public class BatchCommands {
    private final JobLauncher jobLauncher;

    private final Job migrateDataJob;

    private final JobFinishHelper jobFinishListener;

    @Command(command = "start batch", alias = "sb", description = "Start migration from Mongo to Postgres")
    public String startMigration() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            JobExecution jobExecution = jobLauncher.run(migrateDataJob, jobParameters);

            if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
                jobFinishListener.afterJob(jobExecution);
            }

            return "Job finished with code: " + jobExecution.getStatus();
        } catch (JobExecutionException e) {
            return "Job execution failed: " + e.getMessage();
        }
    }
}
