package com.galaxy13.hw.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.StringJoiner;

@RequiredArgsConstructor
@Data
public class Question {
    private final String text;

    private final List<Answer> answers;

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner("\n");
        sj.add(text);
        for (Answer a : answers) {
            sj.add("- " + a.text() + (a.isCorrect() ? ", correct answer" : ""));
        }
        return sj.toString();
    }
}
