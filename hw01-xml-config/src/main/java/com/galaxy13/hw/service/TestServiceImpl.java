package com.galaxy13.hw.service;

import com.galaxy13.hw.dao.QuestionDao;
import com.galaxy13.hw.domain.Answer;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        questionDao.findAll().forEach(
                question -> {
                    ioService.printLine(question.text());
                    List<Answer> answers = question.answers();
                    for (int i = 0; i < answers.size(); i++) {
                        Answer answer = answers.get(i);
                        ioService.printFormattedLine(i + 1 + ". %s%s", answer.text(),
                                answer.isCorrect() ? "; correct" : "");
                    }
                }
        );
    }
}
