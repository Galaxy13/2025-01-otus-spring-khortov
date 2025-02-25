package com.galaxy13.hw.service;

public interface IOService {
    void printLine(String line);

    @SuppressWarnings("unused")
    void printFormattedLine(String format, Object... args);
}
