package ui;

import processor.Processor;
import util.Question;
import util.UserGameChoices;

import java.util.*;

public class CommandLine {
    private final Processor processor;
    private final Scanner scan;
    private final Map<Integer, String> categories;
    private final Map<Integer, String> difficulties;
    private Queue<Question> questionQueue = null;

    /**
     * CommandLine objects which creates a new CommandLine UI.
     * Initializes a grouping of categories and difficulties.
     * @param processor
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
        int questionCount = getResponseToQuestion("How many questions would you like?", 5, 50);
        String categoryQuestion = """
                Which category would you like? Enter the number corresponding to your chosen category.
                 1 - General Knowledge
                 2 - Entertainment
                 3 - Science
                 4 - History and The World
                 5 - Random""";
        int category = getResponseToQuestion(categoryQuestion, 1, 5);
        String difficultyQuestion = """
                What level of difficulty would you like? Enter the number corresponding to your chosen category.
                 1 - Easy
                 2 - Medium
                 3 - Hard
                 4 - Mixed Difficulty""";
        int difficulty = getResponseToQuestion(difficultyQuestion, 1, 4);
        System.out.println("You will be asked " + questionCount + " " + difficulties.get(difficulty) + " difficulty " +
                categories.get(category) + " questions.");
        System.out.println("Beginning game...\n");
        UserGameChoices choices = new UserGameChoices(questionCount, category, difficulty);
        System.out.println("URLs to Parse: " + processor.makeQueryURLs(choices)); //TODO: remove after implementing
        List<String> queries = processor.makeQueryURLs(choices);        //Process all queries to parseable URLs
        questionQueue = processor.buildQuestions(queries);              //Process all queries and convert to Question type
        questionAndAnswer();
        scan.close();
    }

    /**
     * Prompt user with questions, retrieve their answers, until the game is exhausted.
     */
    private void questionAndAnswer() {
        int correctAnswers = 0;
        int incorrectAnswers = 0;

        //Remove questions from queue, prompt answer, repeat
        while (questionQueue.size() > 0) {
            Question currentQuestion = questionQueue.remove();

            //Jumble up response options
            List<String> answers = currentQuestion.getIncorrectAnswers();
            answers.add(currentQuestion.getCorrectAnswer());
            System.out.println("Pre shuffle: " + answers);
            Collections.shuffle(answers);

            //Maps ints to answers so we can track entries
            Map<Integer, String> answerMap = new HashMap<Integer, String>();
            for (int i = 1; i < answers.size() + 1; i++) {
                answerMap.put(i, answers.get(i - 1));
            }
            System.out.println("Post shuffle: " + answerMap);

            int response = getResponseToQuestion(currentQuestion, answerMap);

            //Check if response is correct; increment corrects if so, incorrects otherwise
            if (answerMap.get(response).equals(currentQuestion.getCorrectAnswer())) {
                correctAnswers++;
            } else {
                incorrectAnswers++;
            }
        }

        printStatistics(correctAnswers, incorrectAnswers);
    }

    /**
     * Retrieve a response to each question from the user.
     * @param question
     * @param lowerBound
     * @param upperBound
     * @return
     */
    private int getResponseToQuestion(String question, int lowerBound, int upperBound) {
        int response = 0;
        boolean answered = false;
        while (!answered) {
            System.out.println("\n" + question);
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
     * TODO: Possibly refactor for cleanliness/fewer lines of code. Might violate DRY.
     * @param question
     * @return
     */
    private int getResponseToQuestion(Question question, Map<Integer, String> answers) {
        if (question == null) return -1;
        int lowerBound = 1;
        int upperBound = question.getIncorrectAnswers().size() + 1;
        //Get Question text, set lowerbound to 1, and get the upperbound by number of incorrect answers + 1
        int response = 0;
        boolean answered = false;
        while (!answered) {
            System.out.println("\n" + question);
            //Print out mapped questions
            for (int i = 1; i < answers.size() + 1; i++) {
                System.out.println((i) + ". " + answers.get(i));
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
     * Displays statistics for the game round.
     * @param correctAnswers
     * @param incorrectAnswers
     */
    private void printStatistics(int correctAnswers, int incorrectAnswers) {
        System.out.println("You answered " + correctAnswers + " questions correctly and " + incorrectAnswers + " incorrectly.");

    }
}
