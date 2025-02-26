package com.galaxy13.hw.config;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
@Getter
@Accessors(fluent = true)
public class AppProperties implements TestConfig, TestFileNameProvider {

    @Value("${test.fileName}")
    private String testFileName;

    @Value("${test.passPercentage}")
    private int rightAnswersPercentageToPass;
}
