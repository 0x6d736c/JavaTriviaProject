package util;

public class Question {
    private final String questionText;
    private final String correctAnswer;
    private final String[] incorrectAnswers;
    private final int difficulty;
    private final int category;

    public Question(String questionText, String correctAnswer, String[] incorrectAnswers, int difficulty,
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
