import processor.Processor;
import ui.CommandLine;

public class Main {
    /**
     * The main class for the Java Trivia Project. Coordinates all of the constituent parts of the program:
     * backend, GUI, etc. to provide a smooth experience.
     */
    public static void main(String[] args) {
        // Drives the program. Or drives us insane. We'll see.
        Processor processor = new Processor();
        CommandLine ui = new CommandLine(processor);
        ui.start();
    }
}
