package com.galaxy13.hw.service;

import com.galaxy13.hw.dao.QuestionDao;
import com.galaxy13.hw.domain.Answer;
import com.galaxy13.hw.domain.Question;
import com.galaxy13.hw.domain.Student;
import com.galaxy13.hw.domain.TestResult;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question: questions) {
            ioService.printLine(question.text());
            List<Answer> answers = question.answers();
            int validAnswerIndex = -1;
            for (int i = 0; i < answers.size(); i++) {
                Answer answer = answers.get(i);
                if (answer.isCorrect()) {
                    validAnswerIndex = i;
                }
                ioService.printFormattedLine(i + 1 + ". %s%s", answer.text());
            }
            int answerIndex = ioService.readIntForRange(1, answers.size(), "Number is out of range. Try again!");
            var isAnswerValid = validAnswerIndex == answerIndex;
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }
}
