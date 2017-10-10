package blockchain;

import visual.BinaryTreeView;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

public class MainHandler {

  private static final String EXIT_COMMAND = "exit";

  private AVLTree<Integer> avlTree;
  private Blockchain<Integer> blockchain;
  private HashMap<Integer, LinkedList<Integer>> hashMap;
  private BinaryTreeView<Integer> binaryTreeView;

  public MainHandler() {
    this.avlTree = new AVLTree<>();
    this.blockchain = new Blockchain<>();
    this.hashMap = new HashMap<>();
    try {
      this.binaryTreeView = new BinaryTreeView<Integer>(avlTree.getRoot(), 500, 500);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public void runCode() {
    String input;
    boolean zerosSet = false;

    try {
      while (true) {

        if (!zerosSet) {
          System.out.println("Ingese la cantidad de zeros: ");
          input = ConsoleReader.readingFromConsole();

          if (input.matches("^\\d*$")) {
            Integer aux = Integer.parseInt(input.substring(0));
            blockchain.setZeros(aux);
            zerosSet = true;

          } else {
            System.out.println("No se ingreso un numero.");
          }

        } else {
          System.out.println("Enter some text, or '" + EXIT_COMMAND + "' to quit");
          System.out.print("> ");

          input = ConsoleReader.readingFromConsole();

          if (input.matches("^(add\\s\\d*)$")) {
            System.out.println("Agregaste un elemento");
            Integer aux = Integer.parseInt(input.substring(4));
            System.out.println("Voy a agregar este nodo: " + aux);
            try {
              avlTree.insert(aux);
              blockchain.add(aux, true);
            } catch (DuplicateNodeInsertException e) {
              e.printStackTrace();
              blockchain.add(aux, false);
            }

          } else if (input.matches("^(lookup\\s\\d*)$")) {
            System.out.println("Buscaste un elemento");
            Integer aux = Integer.parseInt(input.substring(7));
            System.out.println("Voy a buscar este nodo: " + aux);

          } else if (input.matches("^(remove\\s\\d*)$")) {
            System.out.println("Borraste un elemento");
            Integer aux = Integer.parseInt(input.substring(7));
            System.out.println("Borraste este nodo: " + aux);
            try {
              avlTree.delete(aux);
              blockchain.add(aux, true);
            } catch (NodeNotFoundException e) {
              e.printStackTrace();
              blockchain.add(aux, false);
            }

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
          binaryTreeView.refresh(avlTree.getRoot());
        }
      }
    } catch (IOException e) {
      System.out.println("Exception has been thrown");
    }
  }


}
