package processor;

import util.UserGameChoices;

import java.util.*;

public class Processor {
    private Map<Integer, Integer[]> categoryIDs;

    public Processor() {
        initializeCategoriesMap();
    }

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
}
