package util;

import java.util.List;

public class Question {
    private final String questionText;
    private final String correctAnswer;
    private final List<String> incorrectAnswers;
    private final int difficulty;
    private final int category;

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public List<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    /**
     * Question object containing question data.
     * @param questionText - a String representing the question text
     * @param correctAnswer - a String representing the correct answer to the question
     * @param incorrectAnswers - a String array of common incorrect answers
     * @param difficulty - an int representing question difficulty
     * @param category - an int representing the question category
     */
    public Question(String questionText, String correctAnswer, List<String> incorrectAnswers, int difficulty,
                    int category) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswers = incorrectAnswers;
        this.difficulty = difficulty;
        this.category = category;
    }

    public String toString() {
        return questionText;
    }
}
