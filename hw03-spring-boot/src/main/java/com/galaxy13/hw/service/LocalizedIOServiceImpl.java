package com.galaxy13.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocalizedIOServiceImpl implements LocalizedIOService {
    private final LocalizedMessageService localizedMessageService;

    @Qualifier("loggerIOService")
    private final IOService ioService;

    @Override
    public void printLineLocalized(String code) {
        ioService.printLine(localizedMessageService.getMessage(code));
    }

    @Override
    public void printFormattedLineLocalized(String code, Object... args) {
        ioService.printLine(localizedMessageService.getMessage(code, args));
    }

    @Override
    public String readStringWithPromptLocalized(String promptCode) {
        return ioService.readStringWithPrompt(localizedMessageService.getMessage(promptCode));
    }

    @Override
    public int readIntForRangeLocalized(int min, int max, String errorMessageCode) {
        return ioService.readIntForRange(min, max, localizedMessageService.getMessage(errorMessageCode));
    }

    @Override
    public int readIntForRangeWithPromptLocalized(int min, int max, String promptCode, String errorMessageCode) {
        return ioService.readIntForRangeWithPrompt(min,
                max,
                localizedMessageService.getMessage(promptCode, max),
                localizedMessageService.getMessage(errorMessageCode));
    }

    @Override
    public void printLine(String line) {
        ioService.printLine(line);
    }

    @Override
    public void printFormattedLine(String format, Object... args) {
        ioService.printFormattedLine(format, args);
    }

    @Override
    public String readStringWithPrompt(String prompt) {
        return ioService.readStringWithPrompt(prompt);
    }

    @Override
    public String readString() {
        return ioService.readString();
    }

    @Override
    public int readIntForRange(int min, int max, String errorMessage) {
        return ioService.readIntForRange(min, max, errorMessage);
    }

    @Override
    public int readIntForRangeWithPrompt(int min, int max, String prompt, String errorMessage) {
        return ioService.readIntForRangeWithPrompt(min, max, prompt, errorMessage);
    }
}
