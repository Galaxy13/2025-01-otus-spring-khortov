package com.galaxy13.hw.service;

import com.galaxy13.hw.exception.QuestionReadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.IllegalFormatException;
import java.util.Scanner;

@Service
@Slf4j
public class LoggerIOService implements IOService {
    private static final int MAX_ATTEMPTS = 10;

    private final Scanner scanner;

    public LoggerIOService(@Value("#{T(System).in}") InputStream inputStream) {
        scanner = new Scanner(new BufferedInputStream(inputStream));
    }

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

    @Override
    public String readStringWithPrompt(String prompt) {
        printLine(prompt);
        return scanner.nextLine();
    }

    @Override
    public String readString() {
        return scanner.nextLine();
    }

    @Override
    public int readIntForRange(int min, int max, String errorMessage) {
        for (int i = 0; i < MAX_ATTEMPTS; i++) {
            try {
                var stringValue = scanner.nextLine();
                int intValue = Integer.parseInt(stringValue);
                if (intValue < min || intValue > max) {
                    throw new IllegalArgumentException();
                }
                return intValue;
            } catch (IllegalArgumentException e) {
                printLine(errorMessage);
            }
        }
        throw new IllegalArgumentException("Error during reading int value");
    }
}
