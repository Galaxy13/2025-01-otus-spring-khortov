package com.galaxy13.hw.shell;

import com.galaxy13.hw.service.TestRunnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.command.annotation.Command;

@Command
@RequiredArgsConstructor
public class TestShell {

    private final TestRunnerService testRunnerService;

    @Command(command = "start")
    public void start() {
        testRunnerService.run();
    }
}
