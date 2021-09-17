package processor;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import datamanagement.QueryParser;
import util.Question;
import util.UserGameChoices;

import java.lang.reflect.Type;
import java.util.*;

public class Processor {
    private Map<Integer, Integer[]> categoryIDs;

    public Processor() {
        initializeCategoriesMap();
    }

    /**
     * Initializes a HashMap containing category groupings mapped to categories. Each category grouping
     * contains at least one, but can contain multiple, question categories.
     */
    private void initializeCategoriesMap() {
        categoryIDs = new HashMap<>();

        // General Knowledge
        categoryIDs.put(1, new Integer[] {9});

        // Entertainment (Books, Film, Music, Theater, TV, Video Games, Board Games, Sports, Celebrities, Comics, Anime, Cartoons)
        categoryIDs.put(2, new Integer[] {10, 11, 12, 13, 14, 15, 16, 21, 26, 29, 31, 32});

        // Science (Science & Nature, Computers, Mathematics, Animals, Gadgets)
        categoryIDs.put(3, new Integer[] {17, 18, 19, 27, 30});

        // History and the World (Mythology, Geography, History, Politics)
        categoryIDs.put(4, new Integer[] {20, 22, 23, 24});
    }

    /**
     * Takes the total number of questions to display and the number of subcategories as inputs,
     * determines the number of questions each subcategory is allocated.
     *
     * Fills array with int representing number of questions per subcategory
     * If the number doesn't divide evenly, iterate over array the remainder number of times
     * and add +1 to each successive subcategory count.
     * @param questionCount - an integer representing the number of questions the user requests.
     * @param subcategoryCount - an integer representing the number of subcategories in the category that was requested.
     * @return questionsPerSubcategory - an integer array containing the number of questions per subcategory.
     */
    private static int[] calculateQuestionsPerSubcategory(int questionCount, int subcategoryCount) {
        int[] questionsPerSubcategory = new int[subcategoryCount];
        int questionsEach = questionCount / subcategoryCount;
        int remainder = questionCount % subcategoryCount;
        Arrays.fill(questionsPerSubcategory, questionsEach);
        for (int i = 0; i < remainder; i++) {
            questionsPerSubcategory[i]++;
        }
        return questionsPerSubcategory;
    }

    /**
     * Generates the necessary query URLs to be used to retrieve questions from the API.
     * @param choices - a UserGameChoices object containing the user's game settings.
     * @return urls - a List of type String returning the query URLs to be used for API calls.
     */
    public List<String> makeQueryURLs(UserGameChoices choices) {
        List<String> urls = new ArrayList<>();
        int questionCount = choices.getQuestionCount(), category = choices.getCategory(), difficultyChoice = choices.getDifficulty();

        StringBuilder baseURL = new StringBuilder("https://opentdb.com/api.php?type=multiple");
        if (difficultyChoice != 4) {
            baseURL.append("&difficulty=");
            if (difficultyChoice == 1) {
                baseURL.append("easy");
            } else if (difficultyChoice == 2) {
                baseURL.append("medium");
            } else {
                baseURL.append("hard");
            }
        }

        if (category != 5) {
            int[] questionsPerSubcategory = calculateQuestionsPerSubcategory(questionCount, categoryIDs.get(category).length);
            baseURL.append("&category=");
            StringBuilder subcategoryURL;
            for (int i = 0; i < questionsPerSubcategory.length; i++) {
                subcategoryURL = new StringBuilder(baseURL);
                subcategoryURL.append(categoryIDs.get(category)[i]);
                subcategoryURL.append("&amount=").append(questionsPerSubcategory[i]);
                urls.add(subcategoryURL.toString());
            }
        } else {
            baseURL.append("&amount=").append(questionCount);
            urls.add(baseURL.toString());
        }

        return urls;
    }

    /**
     * Loops through the List of query strings and generates a question for each one, appending it to
     * the master list of questions. Returns a queue of Question types.
     * @param urls - the query strings to be parsed into questions
     * @return A queue of Question objects to ask the user
     */
    public Queue<Question> buildQuestions(List<String> urls) {
        List<Question> questions = new ArrayList<>();

        // Loop over every query URL
        for (String url : urls) {
            JsonArray queryResult = QueryParser.parse(url); // Returns a JsonArray containing every question from the query

            // If query result has multiple questions, loop over and add all of them
            if (queryResult != null) {
                for (int i = 0; i < queryResult.size(); i++) {
                    String question = queryResult.get(i).getAsJsonObject().get("question").getAsString();
                    String correctAnswer = queryResult.get(i).getAsJsonObject().get("correct_answer").getAsString();
                    Type listType = new TypeToken<List<String>>() {
                    }.getType();
                    List<String> incorrectAnswers = new Gson().fromJson(queryResult.get(i).getAsJsonObject().get("incorrect_answers"), listType);
                    String difficulty = queryResult.get(i).getAsJsonObject().get("difficulty").getAsString();
                    String category = queryResult.get(i).getAsJsonObject().get("category").getAsString();
                    questions.add(new Question(question, correctAnswer, incorrectAnswers, difficulty, category));
                }
            }
        }

        // Shuffle and return the questions as a Queue
        Collections.shuffle(questions);
        return new LinkedList<>(questions);
    }
}
