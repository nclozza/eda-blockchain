import blockchain.AVLTree;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import blockchain.ConsoleReader;

public class Main {

  private static final String EXIT_COMMAND = "exit";

  public static void main(String[] args) {

    String input;

    try {
      System.out.println("Enter some text, or '" + EXIT_COMMAND + "' to quit");
      while (true) {
        System.out.print("> ");

        input = ConsoleReader.readingFromConsole();

        if (input.matches("^(add\\s\\d*)$")) {
          System.out.println("Agregaste un elemento");
          Integer aux = Integer.parseInt(input.substring(4));
          System.out.println("Voy a agregar este nodo: " + aux);
        } else if (input.matches("^(lookup\\s\\d*)$")) {
          System.out.println("Buscaste un elemento");
          Integer aux = Integer.parseInt(input.substring(7));
          System.out.println("Voy a buscar este nodo: " + aux);
        } else if (input.matches("^(remove\\s\\d*)$")) {
          System.out.println("Borraste un elemento");
          Integer aux = Integer.parseInt(input.substring(7));
          System.out.println("Borraste este nodo: " + aux);

        } else if (input.matches("^(zeros\\s\\d*)$")) {
          System.out.println("Seteaste los zeros");
          Integer aux = Integer.parseInt(input.substring(6));
          System.out.println("Seteaste esta cantidad de zeros: " + aux);

        } else if (input.matches("^(validate)$")) {
          System.out.println("Quisiste validar");

        } else if (input.matches("^(modify)$")) {
          System.out.println("Quisiste modificar archivo, todavia no esta listo");

        } else if (input.matches("^(exit)$")) {
          System.out.println("Nos vemos");
          break;
        } else {
          System.out.println("Horrible input");
        }
      }
    } catch (IOException e) {
      System.out.println("Exception has been thrown");
    }
  }
}
