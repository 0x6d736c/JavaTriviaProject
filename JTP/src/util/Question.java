package util;

import org.apache.commons.text.*;

import java.util.List;

public class Question {
    private String questionText;
    private String correctAnswer;
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
        escapeStrings();
        obscureCorrectAnswers();
    }

    /**
     * Removes giveaway quotes from correct answers.
     * The API provides answers with quotations surrounding them,
     * this function removes them to obscure the correct answer from the user.
     */
    private void obscureCorrectAnswers() {
        this.correctAnswer = this.correctAnswer.replaceAll("^\"|\"$", "");
    }

    /**
     * Escapes the Strings from HTML4 and removes/sanitizes wonky data.
     * e.g. &quot should become an actual quotation mark
     */
    private void escapeStrings() {
        this.questionText = StringEscapeUtils.unescapeHtml4(this.questionText);
        this.correctAnswer = StringEscapeUtils.unescapeHtml4(this.correctAnswer);
        for (int i = 0; i < this.incorrectAnswers.size(); i++) {
            //Modifies list in-place to escape any HTML4 holdover characters
            this.incorrectAnswers.set(i, StringEscapeUtils.unescapeHtml4(this.incorrectAnswers.get(i)));
        }
    }

    public String toString() {
        return questionText;
    }
}
