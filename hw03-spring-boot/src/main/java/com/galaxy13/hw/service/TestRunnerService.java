package com.galaxy13.hw.service;

import org.springframework.boot.CommandLineRunner;

public interface TestRunnerService extends CommandLineRunner {
    void run(String... args) throws Exception;
}
