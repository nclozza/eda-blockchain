package blockchain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The ConsoleReader class is used to read user input from the console to act upon the AVL tree.
 */
public class ConsoleReader {
    public static String readingFromConsole() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        return br.readLine();
    }
}
