package com.galaxy13.hw.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
public record AppProperties(@Value("${passPercentage}") int rightAnswersPercentageToPass,
                            @Value("${fileName}") String testFileName) implements TestConfig, TestFileNameProvider {
}
