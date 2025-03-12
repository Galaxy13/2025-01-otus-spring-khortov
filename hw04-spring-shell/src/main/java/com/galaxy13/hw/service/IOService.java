package com.galaxy13.hw.service;

public interface IOService {
    void printLine(String line);

    @SuppressWarnings("unused")
    void printFormattedLine(String format, Object... args);

    String readStringWithPrompt(String prompt);

    String readString();

    int readIntForRange(int min, int max, String errorMessage);

    int readIntForRangeWithPrompt(int min, int max, String prompt, String errorMessage);
}
