package com.galaxy13.hw.dao;

import com.galaxy13.hw.config.TestFileNameProvider;
import com.galaxy13.hw.dao.dto.QuestionCsvDto;
import com.galaxy13.hw.domain.Question;
import com.galaxy13.hw.exception.QuestionReadException;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Repository
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider testFileNameProvider;

    @Override
    public List<Question> findAll() {
        try (InputStream inputStream = this.getClass().getResourceAsStream(testFileNameProvider.testFileName());
             BufferedReader resource = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream,
                     "CSV file not found: " + testFileNameProvider.testFileName())))) {
            return new CsvToBeanBuilder<QuestionCsvDto>(resource)
                    .withType(QuestionCsvDto.class)
                    .withSkipLines(1)
                    .withSeparator(';')
                    .build()
                    .parse()
                    .stream()
                    .map(QuestionCsvDto::toDomainObject)
                    .toList();
        } catch (IOException e) {
            throw new QuestionReadException("Failed to read test file", e);
        }
    }
}
