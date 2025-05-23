package com.galaxy13.hw.service;

import com.galaxy13.hw.dao.QuestionDao;
import com.galaxy13.hw.domain.Answer;
import com.galaxy13.hw.domain.Question;
import com.galaxy13.hw.domain.Student;
import com.galaxy13.hw.domain.TestResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLineLocalized("TestService.answer.the.questions");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question : questions) {
            handleQuestion(question, testResult);
        }
        return testResult;
    }

    private void outputAnswers(List<Answer> answers) {
        for (int i = 0; i < answers.size(); i++) {
            Answer answer = answers.get(i);
            ioService.printFormattedLine(i + 1 + ". %s", answer.text());
        }
    }

    private void handleQuestion(Question question, TestResult testResult) {
        List<Answer> answers = question.answers();
        ioService.printLine(question.text());
        boolean isUserAnswerCorrect;
        if (answers.size() > 1) {
            outputAnswers(answers);
            int userAnswerIndex = ioService.readIntForRangeWithPromptLocalized(1,
                    answers.size(),
                    "TestService.answer.range",
                    "TestService.answer.error.range") - 1;
            isUserAnswerCorrect = question.answers().get(userAnswerIndex).isCorrect();
        } else {
            String correctAnswer = answers.getFirst().text();
            String userAnswer = ioService.readStringWithPromptLocalized("TestService.answer.full.answer")
                    .trim();
            isUserAnswerCorrect = correctAnswer.equals(userAnswer);
        }
        testResult.applyAnswer(question, isUserAnswerCorrect);
    }
}
