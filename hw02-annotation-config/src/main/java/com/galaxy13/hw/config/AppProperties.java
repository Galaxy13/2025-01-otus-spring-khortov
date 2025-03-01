package com.galaxy13.hw.config;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
@Accessors(fluent = true)
public class AppProperties implements TestConfig, TestFileNameProvider {

    @Value("${test.fileName}")
    private String testFileName;

    @Value("${test.passPercentage}")
    private int rightAnswersPercentageToPass;
}
