package com.galaxy13.hw.service;

import com.galaxy13.hw.exception.QuestionReadException;
import lombok.extern.slf4j.Slf4j;

import java.util.IllegalFormatException;

@Slf4j
public class LoggerIOService implements IOService {
    @Override
    public void printLine(String line) {
        log.info(line);
    }

    @Override
    public void printFormattedLine(String format, Object... args) {
        String formatted;
        try {
            formatted = String.format(format, args);
        } catch (IllegalFormatException e) {
            throw new QuestionReadException("Incompatible line format", e);
        }
        log.info(formatted);
    }
}
