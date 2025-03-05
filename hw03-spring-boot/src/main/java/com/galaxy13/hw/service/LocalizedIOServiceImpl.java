package com.galaxy13.hw.service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocalizedIOServiceImpl implements LocalizedIOService {
    private final LocalizedMessageService localizedMessageService;

    @Qualifier("loggerIOService")
    @Delegate(types = IOService.class)
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
}
