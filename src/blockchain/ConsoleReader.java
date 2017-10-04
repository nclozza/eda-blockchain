package blockchain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleReader {

    public static final String EXIT_COMMAND = "exit";

    public static void readingFromConsole() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter some text, or '" + EXIT_COMMAND + "' to quit");

        while (true) {
            System.out.print("> ");
            String input = br.readLine();

            if (input.length() == EXIT_COMMAND.length() && input.toLowerCase().equals(EXIT_COMMAND)) {
                System.out.println("Exiting.");
                return;
            }
            System.out.println("The input was: " + input);
        }
    }
}
