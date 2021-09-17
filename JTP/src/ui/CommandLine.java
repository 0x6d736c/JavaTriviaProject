package ui;

import org.apache.commons.lang3.StringUtils;
import processor.Processor;
import util.Question;
import util.UserGameChoices;

import java.util.*;

public class CommandLine {
    private final Processor processor;
    private final Scanner scan;
    private final Map<Integer, String> categories;
    private final Map<Integer, String> difficulties;
    private Queue<Question> questionQueue;

    /**
     * CommandLine object constructor which creates a new CommandLine UI.
     * Initializes a grouping of categories and difficulties.
     * @param processor A processor object from the processing tier
     */
    public CommandLine(Processor processor) {
        this.processor = processor;
        scan = new Scanner(System.in);
        categories = new HashMap<>();
        categories.put(1, "general knowledge");
        categories.put(2, "entertainment");
        categories.put(3, "science");
        categories.put(4, "history and the world");
        categories.put(5, "random");
        difficulties = new HashMap<>();
        difficulties.put(1, "easy");
        difficulties.put(2, "medium");
        difficulties.put(3, "hard");
        difficulties.put(4, "mixed");
    }

    /**
     * Begins a CLI for user input/game play.
     */
    public void start() {
        System.out.println("Welcome to Trivia!");
        boolean playing = true;
        String playAgainQuestion = """
                Would you like to play again? Enter the number corresponding to your choice.
                 1 - Yes
                 2 - No""";
        while (playing) {
            setUpGame();
            playOneGame();
            playing = getResponseToQuestion(playAgainQuestion, 1, 2, false) == 1;
        }
        System.out.println("\nSee you next time!");
        scan.close();
    }

    /**
     * Sets the question queue field to a new set of questions corresponding to the user's choices
     */
    private void setUpGame() {
        int questionCount = getResponseToQuestion("How many questions would you like? (5-50)", 5, 50, false);
        String categoryQuestion = """
                Which category would you like? Enter the number corresponding to your chosen category.
                 1 - General Knowledge
                 2 - Entertainment
                 3 - Science
                 4 - History and The World
                 5 - Random""";
        int category = getResponseToQuestion(categoryQuestion, 1, 5, false);
        String difficultyQuestion = """
                What level of difficulty would you like? Enter the number corresponding to your chosen category.
                 1 - Easy
                 2 - Medium
                 3 - Hard
                 4 - Mixed Difficulty""";
        int difficulty = getResponseToQuestion(difficultyQuestion, 1, 4, false);
        System.out.println("\nYou will be asked " + questionCount + " " + difficulties.get(difficulty) + " difficulty " +
                categories.get(category) + " questions.");
        System.out.println("Beginning game...");
        UserGameChoices choices = new UserGameChoices(questionCount, category, difficulty);
        List<String> queries = processor.makeQueryURLs(choices);        // Process all queries to parsable URLs
        questionQueue = processor.buildQuestions(queries);              // Convert all queries to question objects
    }

    /**
     * Prompt user with questions and retrieve their answers until all the questions have been exhausted.
     */
    private void playOneGame() {
        int correctAnswers = 0;
        int incorrectAnswers = 0;

        // Remove questions from queue, prompt answer, repeat
        while (!questionQueue.isEmpty()) {
            Question currentQuestion = questionQueue.poll();

            // Jumble up response options
            List<String> answers = currentQuestion.getIncorrectAnswers();
            String correctAnswer = currentQuestion.getCorrectAnswer();
            answers.add(correctAnswer);
            Collections.shuffle(answers);
            int correctAnswerIndex = answers.indexOf(correctAnswer);
            int response = getResponseToTriviaQuestion(currentQuestion, answers);

            // Check if response is correct then increment correct or incorrect answers
            if (response - 1 == correctAnswerIndex) {
                System.out.println("Correct!");
                correctAnswers++;
            } else {
                System.out.print("Incorrect! ");
                System.out.println("The correct answer was " + correctAnswer + ".");
                incorrectAnswers++;
            }
        }

        printStatistics(correctAnswers, incorrectAnswers);
    }

    /**
     * Retrieve a response to each question from the user.
     * @param question Question to ask
     * @param lowerBound Lower bound on what the user can answer
     * @param upperBound Lower bound on what the user can answer
     * @return The user's choice
     */
    private int getResponseToQuestion(String question, int lowerBound, int upperBound, boolean isTriviaQuestion) {
        int response = 0;
        boolean answered = false;
        while (!answered) {
            if (!isTriviaQuestion) {
                System.out.println("\n" + question);
            }
            System.out.print("> ");
            try {
                response = scan.nextInt();
                while (response < lowerBound || response > upperBound) {
                    System.out.println("Please enter a number from " + lowerBound + " to " + upperBound + ".");
                    System.out.print("> ");
                    response = scan.nextInt();
                }
                answered = true;
            } catch (InputMismatchException e) {
                System.out.println("Please only enter a number.");
                scan.nextLine();
            }
        }
        return response;
    }

    /**
     * Asks the user a trivia question and gets their response
     * @param question Question object to ask
     * @return The user's response
     */
    private int getResponseToTriviaQuestion(Question question, List<String> answers) {
        System.out.println("\nCategory: " + question.getCategory() + "; Difficulty: " + StringUtils.capitalize(question.getDifficulty()));
        System.out.println(question);
        for (int i = 1; i <= 4; i++) {
            System.out.println(i + ". " + answers.get(i - 1));
        }
        return getResponseToQuestion(null, 1, 4, true);
    }

    /**
     * Displays statistics for the game round.
     * @param correctAnswers Number of correct answers
     * @param incorrectAnswers Number of incorrect answers
     */
    private void printStatistics(int correctAnswers, int incorrectAnswers) {
        System.out.println("You answered " + correctAnswers + " questions correctly and " + incorrectAnswers + " incorrectly.");
    }
}
