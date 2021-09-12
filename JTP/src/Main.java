import com.google.gson.JsonArray;
import datamanagement.QueryParser;
import processor.Processor;
import ui.CommandLine;

import java.util.ArrayList;
import java.util.List;

public class Main {
    /**
     * The main class for the Java Trivia Project. Coordinates all of the consituent parts of the program:
     * backend, GUI, etc. to provide a smooth experience.
     */
    public static void main(String[] args) {
//        // Drives the program. Or drives us insane. We'll see.
//
//        //TODO: Move this to the appropriate architectural level later.
//        String query = "https://opentdb.com/api.php?amount=50&difficulty=hard&type=multiple";                         //Contains the primary query, append as necessary
//
//        JsonArray queryResult = QueryParser.parse(query);
//        System.out.println(queryResult);
        Processor processor = new Processor();
        CommandLine ui = new CommandLine(processor);
        ui.start();
    }
}
