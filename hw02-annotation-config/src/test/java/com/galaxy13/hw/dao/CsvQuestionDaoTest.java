package com.galaxy13.hw.dao;

import com.galaxy13.hw.config.TestFileNameProvider;
import com.galaxy13.hw.domain.Answer;
import com.galaxy13.hw.domain.Question;
import com.galaxy13.hw.exception.QuestionReadException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class CsvQuestionDaoTest {
    private TestFileNameProvider fileNameProvider;

    private QuestionDao dao;

    @BeforeEach
    void setUp() {
        this.fileNameProvider = Mockito.mock(TestFileNameProvider.class);
        this.dao = new CsvQuestionDao(fileNameProvider);
    }

    @Test
    void throwsExceptionOnAbsentFileTest() {
        when(fileNameProvider.testFileName()).thenReturn("/absentFile.csv");
        assertThatThrownBy(() -> dao.findAll()).isInstanceOf(NullPointerException.class);

        when(fileNameProvider.testFileName()).thenReturn(null);
        assertThatThrownBy(() -> dao.findAll()).isInstanceOf(NullPointerException.class);
    }

    @Test
    void returnEmptyListOnEmptyFileTest() {
        when(fileNameProvider.testFileName()).thenReturn("/emptyCsv.csv");
        assertThat(dao.findAll()).isEmpty();
    }

    @Test
    void throwsExceptionOnEmptyAnswersTest() {
        when(fileNameProvider.testFileName()).thenReturn("/emptyAnswers.csv");
        assertThatThrownBy(() -> dao.findAll()).isInstanceOf(QuestionReadException.class);
    }

    @Test
    void throwsExceptionOnEmptyQuestionTest() {
        when(fileNameProvider.testFileName()).thenReturn("/emptyQuestion.csv");
        assertThatThrownBy(() -> dao.findAll()).isInstanceOf(QuestionReadException.class);
    }

    @Test
    void throwsExceptionOnWrongAnswerFormatTest() {
        when(fileNameProvider.testFileName()).thenReturn("/wrongAnswerFormat.csv");
        assertThatThrownBy(() -> dao.findAll()).isInstanceOf(RuntimeException.class);
    }

    @Test
    void returnsListOfQuestionsTest() {
        when(fileNameProvider.testFileName()).thenReturn("/questions.csv");
        List<Question> questions = dao.findAll();
        assertThat(questions).containsExactly(
                new Question("question1",
                        List.of(new Answer("answer1", false),
                                new Answer("answer2", true),
                                new Answer("answer3", false))),
                new Question("question2", List.of(new Answer("answer", true))),
                new Question("question3", List.of(new Answer("test", false),
                        new Answer("test2", true)))
        );
    }
}
