package com.galaxy13.hw.service;

import com.galaxy13.hw.dao.QuestionDao;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        questionDao.findAll()
                .forEach(question -> {
                    ioService.printLine(question.getText());
                    question.getAnswers()
                            .forEach(answer ->
                                    ioService.printFormattedLine("- %s%s", answer.text(),
                                            answer.isCorrect() ? "; correct" : ""));
                });
    }
}
