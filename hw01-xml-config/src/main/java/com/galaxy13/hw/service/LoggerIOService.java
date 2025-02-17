package com.galaxy13.hw.service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggerIOService implements IOService {
    @Override
    public void printLine(String line) {
        log.info(line);
    }

    @Override
    public void printFormattedLine(String format, Object... args) {
        String formatted = String.format(format, args);
        log.info(formatted);
    }
}
