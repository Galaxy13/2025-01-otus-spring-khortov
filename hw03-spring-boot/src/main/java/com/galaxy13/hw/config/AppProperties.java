package com.galaxy13.hw.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Locale;
import java.util.Map;

@ConfigurationProperties(prefix = "application")
@Slf4j
@Getter
public class AppProperties implements TestConfig, TestFileNameProvider, LocaleConfig {

    private final int rightAnswersPercentageToPass;

    @Getter(AccessLevel.NONE)
    private final Map<String, String> fileNameByLocaleTag;

    private final Locale locale;

    public AppProperties(int rightAnswersPercentageToPass,
                         @NonNull Map<String, String> fileNameByLocaleTag,
                         @NonNull Locale locale) {
        if (rightAnswersPercentageToPass == 0) {
            log.warn("Passing percentage is set to zero or not defined in application.yml. Test is unpassable");
        }
        this.rightAnswersPercentageToPass = rightAnswersPercentageToPass;
        this.fileNameByLocaleTag = fileNameByLocaleTag;
        this.locale = locale;
    }

    @Override
    public String testFileName() {
        String fileName = fileNameByLocaleTag.get(locale.toLanguageTag());
        if (fileName == null) {
            log.warn("No appropriate file name for locale {}. Trying using default en_US", locale.toLanguageTag());
            fileName = fileNameByLocaleTag.get(Locale.US.toLanguageTag());
        }
        return "/" + fileName;
    }
}
