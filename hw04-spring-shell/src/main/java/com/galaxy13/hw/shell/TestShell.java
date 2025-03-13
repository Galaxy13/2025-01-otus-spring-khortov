package com.galaxy13.hw.shell;

import com.galaxy13.hw.service.TestRunnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@RequiredArgsConstructor
public class TestShell {

    private final TestRunnerService testRunnerService;

    @ShellMethod(key = "start", value = "Start student test")
    public void start() {
        testRunnerService.run();
    }
}
