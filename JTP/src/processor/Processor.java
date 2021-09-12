package processor;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import datamanagement.QueryParser;
import util.Question;
import util.UserGameChoices;

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
        categoryIDs.put(4, new Integer[] {20, 22, 23, 24, 25});
    }

    /**
     * Takes the total number of questions to display and the number of subcategories as inputs,
     * determines the number of questions each subcategory is allocated.
     *
     * Fills array with int representing number of questions per subcategory
     * If the number doesn't divide evenly, iterate over array {remainder} number of times
     * and add +1 to each successive subcategory count.
     * @param questionCount - an integer representing the number of questions the user requests.
     * @param subcategoryCount - an integer representing the number of subcategories the user requests.
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
     * Generates the necessary query strings to later be used to retrieve questions from API.
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
     * the master list of questions. Returns a List of Question types.
     * @param urls - the query strings to be parsed into questions
     * @return
     */
    public List<Question> buildQuestions(List<String> urls) {
        List<Question> questions = new ArrayList<Question>();

        for (int i = 0; i < urls.size(); i++) {
            JsonArray queryResult = QueryParser.parse(urls.get(i)); //Returns a JsonArray containing every question from the query
            System.out.println(queryResult);
            //If query result has multiple questions, loop over
            for (int j = 0; j < queryResult.size(); j++) {
                String question = queryResult.get(j).getAsJsonObject().get("question").toString();
                String correctAnswer = queryResult.get(j).getAsJsonObject().get("correct_answer").toString();
                List<String> incorrectAnswers = new Gson().fromJson(queryResult.get(j).getAsJsonObject().get("incorrect_answers"), ArrayList.class);
                //TODO: parse question difficulty and category from strings to ints
                questions.add(new Question(
                        question,
                        correctAnswer,
                        incorrectAnswers,
                        0,
                        0
                ));
            }
        }

        return questions;
    }
}
