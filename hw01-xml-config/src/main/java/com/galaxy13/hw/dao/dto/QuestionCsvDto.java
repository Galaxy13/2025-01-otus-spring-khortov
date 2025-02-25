package com.galaxy13.hw.dao.dto;

import com.galaxy13.hw.domain.Answer;
import com.galaxy13.hw.domain.Question;
import com.galaxy13.hw.exception.QuestionReadException;
import com.opencsv.bean.CsvBindAndSplitByPosition;
import com.opencsv.bean.CsvBindByPosition;

import java.util.ArrayList;
import java.util.List;

public class QuestionCsvDto {
    @CsvBindByPosition(position = 0)
    private String question;

    @CsvBindAndSplitByPosition(position = 1, collectionType = ArrayList.class, elementType = Answer.class,
            converter = AnswerCsvConverter.class, splitOn = "\\|")
    private List<Answer> answers;

    public Question toDomainObject() {
        if (question == null || question.isEmpty()) {
            throw new QuestionReadException("Question cannot be empty");
        }
        if (answers == null || answers.isEmpty()) {
            throw new QuestionReadException("Answers cannot be empty");
        }
        return new Question(question, answers);
    }
}
