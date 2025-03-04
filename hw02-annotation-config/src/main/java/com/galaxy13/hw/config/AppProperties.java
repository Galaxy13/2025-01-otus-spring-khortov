package com.galaxy13.hw.config;

import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@lombok.Value
@Accessors(fluent = true)
public class AppProperties implements TestConfig, TestFileNameProvider {
    // 'private' and 'final' modifiers are not needed because of the @lombok.Value annotation.
    // They are added because of checkstyle rule [VisibilityModifier].

    private final String testFileName;

    private final int rightAnswersPercentageToPass;

    public AppProperties(@Value("${test.testFileName}") String testFileName,
                         @Value("${test.rightAnswersPercentageToPass}") int rightAnswersPercentageToPass) {
        this.testFileName = testFileName;
        this.rightAnswersPercentageToPass = rightAnswersPercentageToPass;
    }
}
