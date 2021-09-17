package util;

import org.apache.commons.text.*;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private final String questionText;
    private final String correctAnswer;
    private final List<String> incorrectAnswers;
    private final String difficulty;
    private final String category;

    /**
     * Question object containing question data.
     * Escapes HTML characters from each field to ensure proper printing
     *
     * @param questionText - a String representing the question text
     * @param correctAnswer - a String representing the correct answer to the question
     * @param incorrectAnswers - a String array of common incorrect answers
     * @param difficulty - an int representing question difficulty
     * @param category - an int representing the question category
     */
    public Question(String questionText, String correctAnswer, List<String> incorrectAnswers, String difficulty,
                    String category) {
        this.questionText = StringEscapeUtils.unescapeHtml4(questionText);
        this.correctAnswer = StringEscapeUtils.unescapeHtml4(correctAnswer);
        this.incorrectAnswers = new ArrayList<>();
        for (String incorrectAnswer : incorrectAnswers) {
            this.incorrectAnswers.add(StringEscapeUtils.unescapeHtml4(incorrectAnswer));
        }
        this.difficulty = StringEscapeUtils.unescapeHtml4(difficulty);
        this.category = StringEscapeUtils.unescapeHtml4(category);
    }

    public String toString() {
        return questionText;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public List<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getCategory() {
        return category;
    }
}
