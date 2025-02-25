package com.galaxy13.hw.service;

import com.galaxy13.hw.config.TestConfig;
import com.galaxy13.hw.domain.TestResult;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {
    private final TestConfig testConfig;

    private final IOService ioService;

    @Override
    public void showResult(TestResult testResult) {
        ioService.printLine("");
        ioService.printLine("Test results: ");
        ioService.printFormattedLine("Student: %s", testResult.getStudent().getFullName());
        ioService.printFormattedLine("Answered questions count: %d", testResult.getAnsweredQuestions().size());
        ioService.printFormattedLine("Right answers count: %d", testResult.getRightAnswersCount());
        ioService.printFormattedLine("Percentage: %s%", testResult.getRightAnswersPercentage());

        if (testResult.getRightAnswersPercentage() >= testConfig.rightAnswersPercentageToPass()) {
            ioService.printLine("Congratulations! You passed test!");
            return;
        }
        ioService.printLine("Sorry. You fail test.");
    }
}
