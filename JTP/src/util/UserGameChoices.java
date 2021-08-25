package util;

public class UserGameChoices {
    private final int questionCount;
    private final int category;
    private final int difficulty;

    public UserGameChoices(int questionCount, int category, int difficulty) {
        this.questionCount = questionCount;
        this.category = category;
        this.difficulty = difficulty;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public int getCategory() {
        return category;
    }

    public int getDifficulty() {
        return difficulty;
    }
}
