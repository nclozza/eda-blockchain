import blockchain.AVLTree;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import blockchain.ConsoleReader;

public class Main {

    public static void main(String[] args) {
    	try {
			ConsoleReader.readingFromConsole();
		} catch (IOException e){
    		System.out.println("Exception has been thrown");
		}
    }
}
