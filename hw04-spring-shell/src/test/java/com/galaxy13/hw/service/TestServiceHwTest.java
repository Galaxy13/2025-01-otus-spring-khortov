package com.galaxy13.hw.service;

import com.galaxy13.hw.dao.QuestionDao;
import com.galaxy13.hw.domain.Answer;
import com.galaxy13.hw.domain.Question;
import com.galaxy13.hw.domain.Student;
import com.galaxy13.hw.domain.TestResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static com.galaxy13.hw.TestHelper.constructQuestion;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
class TestServiceHwTest {
    private static final Student STUDENT = Mockito.mock(Student.class);

    @Autowired
    private TestService testService;

    @MockitoBean
    private QuestionDao questionDao;

    @MockitoBean
    private LocalizedIOService ioService;

    @BeforeAll
    static void setStudent() {
        when(STUDENT.firstName()).thenReturn("Rick");
        when(STUDENT.lastName()).thenReturn("Deckard");
        when(STUDENT.getFullName()).thenReturn("Rick Deckard");
    }

    @Test
    void testOneRightQuestion() {
        var testQuestion = new Question(
                "testQuestion", List.of(
                new Answer("testAnswer1", false),
                new Answer("testAnswer2", true)));
        when(questionDao.findAll()).thenReturn(List.of(testQuestion));
        when(ioService.readIntForRangeWithPromptLocalized(ArgumentMatchers.eq(1),
                ArgumentMatchers.eq(2),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString())).thenReturn(2);
        var testResult = testService.executeTestFor(STUDENT);

        assertThat(testResult.getAnsweredQuestions()).containsExactly(testQuestion);
        assertThat(testResult.getRightAnswersCount()).isEqualTo(1);
        assertThat(testResult.getRightAnswersPercentage()).isEqualTo(100);
    }

    @Test
    void testOneWrongQuestion() {
        var testQuestion = new Question(
                "testQuestion", List.of(
                new Answer("testAnswer1", false),
                new Answer("testAnswer2", true)));
        when(questionDao.findAll()).thenReturn(List.of(testQuestion));
        when(ioService.readIntForRangeWithPromptLocalized(ArgumentMatchers.eq(1),
                ArgumentMatchers.eq(2),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString())).thenReturn(1);
        var testResult = testService.executeTestFor(STUDENT);

        assertThat(testResult.getAnsweredQuestions()).containsExactly(testQuestion);
        assertThat(testResult.getRightAnswersCount()).isZero();
        assertThat(testResult.getRightAnswersPercentage()).isZero();
    }

    @Test
    void testOneExactRightQuestion() {
        var exactQuestion = new Question(
                "exactQuestion", List.of(
                new Answer("answer", true)));
        when(questionDao.findAll()).thenReturn(List.of(exactQuestion));
        when(ioService.readStringWithPromptLocalized(ArgumentMatchers.anyString())).thenReturn("answer");
        var exactTestResult = testService.executeTestFor(STUDENT);

        assertThat(exactTestResult.getAnsweredQuestions()).containsExactly(exactQuestion);
        assertThat(exactTestResult.getRightAnswersPercentage()).isEqualTo(100);
        assertThat(exactTestResult.getRightAnswersCount()).isEqualTo(1);
    }

    @Test
    void testOneExactWrongQuestion() {
        var exactQuestion = new Question(
                "exactQuestion", List.of(
                new Answer("answer", true)));

        when(questionDao.findAll()).thenReturn(List.of(exactQuestion));
        when(ioService.readStringWithPromptLocalized(ArgumentMatchers.anyString())).thenReturn("wrong answer");
        var exactTestResult = testService.executeTestFor(STUDENT);

        assertThat(exactTestResult.getAnsweredQuestions()).containsExactly(exactQuestion);
        assertThat(exactTestResult.getRightAnswersPercentage()).isZero();
        assertThat(exactTestResult.getRightAnswersCount()).isZero();
    }

    @Test
    void testUnequalPartsPercentageDivision() {
        var questions = List.of(
                constructQuestion("question1",
                        "answer1", true,
                        "answer2", false),
                constructQuestion("question2",
                        "answer1", true,
                        "answer2", false),
                constructQuestion("question3",
                        "answer1", true,
                        "answer2", false)
        );
        when(questionDao.findAll()).thenReturn(questions);
        when(ioService.readIntForRangeWithPromptLocalized(ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString())).thenReturn(1).thenReturn(2).thenReturn(1);
        var testResult = testService.executeTestFor(STUDENT);

        assertThat(testResult.getAnsweredQuestions()).usingRecursiveComparison().isEqualTo(questions);
        assertThat(testResult.getRightAnswersCount()).isEqualTo(2);
        assertThat(testResult.getRightAnswersPercentage()).isEqualTo(66);
    }

    @Nested
    class FiveQuestionsTest {

        private final List<Question> testQuestions = List.of(
                constructQuestion("question1",
                        List.of("answer11", "answer12", "answer13"), List.of(false, false, true)),
                constructQuestion("question2",
                        "answer21", true,
                        "answer22", false),
                constructQuestion("question3", List.of("answer31", "answer31", "answer33", "answer34"),
                        List.of(false, true, false, false)),
                constructQuestion("question4", "exactAnswer4", true),
                constructQuestion("question5", "exactAnswer5", true)
        );


        private void assertTestResult(TestResult testResult, int correctAnswersNum, int percentage) {
            assertThat(testResult.getAnsweredQuestions()).usingRecursiveComparison().isEqualTo(testQuestions);
            assertThat(testResult.getRightAnswersCount()).isEqualTo(correctAnswersNum);
            assertThat(testResult.getRightAnswersPercentage()).isEqualTo(percentage);
        }

        private void mockAnswers(int answer1, int answer2, int answer3, String answer4, String answer5) {
            when(questionDao.findAll()).thenReturn(testQuestions);
            when(ioService.readIntForRangeWithPromptLocalized(ArgumentMatchers.anyInt(),
                    ArgumentMatchers.anyInt(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                    .thenReturn(answer1, answer2, answer3);
            when(ioService.readStringWithPromptLocalized(ArgumentMatchers.anyString()))
                    .thenReturn(answer4, answer5);
        }

        @Test
        void testFourOutOfFive() {
            mockAnswers(3, 2, 2, "exactAnswer4", "exactAnswer5");
            var testResult = testService.executeTestFor(STUDENT);

            assertTestResult(testResult, 4, 80);
        }

        @Test
        void testFiveOutOfFive() {
            mockAnswers(3, 1, 2, "exactAnswer4", "exactAnswer5");
            var testResult = testService.executeTestFor(STUDENT);
            assertTestResult(testResult, 5, 100);
        }

        @Test
        void testThreeOutOfFive() {
            mockAnswers(3, 1, 2, "wrong ans", "wrong ans");
            var testResult = testService.executeTestFor(STUDENT);
            assertTestResult(testResult, 3, 60);
        }

        @Test
        void testTwoOutOfFive() {
            mockAnswers(1, 2, 2, "wrong ans", "exactAnswer5");
            var testResult = testService.executeTestFor(STUDENT);
            assertTestResult(testResult, 2, 40);
        }

        @Test
        void testOneOutOfFive() {
            mockAnswers(3, 2, 1, "wrong ans", "wrong ans");
            var testResult = testService.executeTestFor(STUDENT);
            assertTestResult(testResult, 1, 20);
        }

        @Test
        void testNoCorrectAnswers() {
            mockAnswers(1, 2, 1, "wrong ans", "wrong ans");
            var testResult = testService.executeTestFor(STUDENT);
            assertTestResult(testResult, 0, 0);
        }
    }
}
