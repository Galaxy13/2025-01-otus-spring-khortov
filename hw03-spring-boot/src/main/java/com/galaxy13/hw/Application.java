package com.galaxy13.hw;

import com.galaxy13.hw.config.AppProperties;
import com.galaxy13.hw.service.TestRunnerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class Application {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Application.class, args);
        TestRunnerService testRunnerService = context.getBean(TestRunnerService.class);

        testRunnerService.run();
    }
}
