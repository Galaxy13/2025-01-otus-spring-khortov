package com.galaxy13.hw;

import com.galaxy13.hw.domain.Answer;
import com.galaxy13.hw.domain.Question;

import java.util.ArrayList;
import java.util.List;

public class TestHelper {
    public static Question constructQuestion(String question, String answer1, boolean isAnswer1Correct,
                                             String answer2, boolean isAnswer2Correct) {
        return new Question(question, List.of(new Answer(answer1, isAnswer1Correct),
                new Answer(answer2, isAnswer2Correct)));
    }

    public static Question constructQuestion(String question, List<String> answersText, List<Boolean> corrects) {
        List<Answer> answers = new ArrayList<>();
        for (int i = 0; i < answersText.size(); i++) {
            answers.add(new Answer(answersText.get(i), corrects.get(i)));
        }
        return new Question(question, answers);
    }

    public static Question constructQuestion(String question, String answer, boolean isCorrect) {
        return new Question(question, List.of(new Answer(answer, isCorrect)));
    }
}
