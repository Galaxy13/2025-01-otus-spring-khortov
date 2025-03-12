package com.galaxy13.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.stereotype.Service;

@Service
@ShellComponent
@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService {
    private final TestService testService;

    private final StudentService studentService;

    private final ResultService resultService;

    @Override
    @ShellMethod(key = "run")
    public void run(@ShellOption(defaultValue = "") String... args) {
        var student = studentService.determineCurrentStudent();
        var testResult = testService.executeTestFor(student);
        resultService.showResult(testResult);
    }
}
