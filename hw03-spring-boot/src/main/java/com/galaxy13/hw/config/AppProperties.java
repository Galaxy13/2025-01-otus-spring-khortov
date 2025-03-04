package com.galaxy13.hw.config;

import com.galaxy13.hw.exception.QuestionReadException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Locale;
import java.util.Map;

@ConfigurationProperties(prefix = "application")
@Slf4j
public class AppProperties implements TestConfig, TestFileNameProvider, LocaleConfig {
    // 'private' and 'final' modifiers are not needed because of the @lombok.Value annotation.
    // They are added because of checkstyle rule [VisibilityModifier].

    @Getter
    private final int rightAnswersPercentageToPass;

    private final Map<String, String> fileNameByLocaleTag;

    @Getter
    private final Locale locale;

    public AppProperties(int rightAnswersPercentageToPass, Map<String, String> fileNameByLocaleTag, Locale locale) {
        if (fileNameByLocaleTag == null || fileNameByLocaleTag.isEmpty()) {
            throw new QuestionReadException("No locale file tag provided");
        }
        if (locale == null) {
            throw new QuestionReadException("No appropriate locale provided");
        }
        if (rightAnswersPercentageToPass == 0) {
            log.warn("Pass percentage is set to zero or not defined in application.yml. Test is unpassable");
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
