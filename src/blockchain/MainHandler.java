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
          System.out.print("Ingese la cantidad de ceros: ");
          input = ConsoleReader.readingFromConsole();

          if (input.matches("^\\d*$")) {
            Integer aux = Integer.parseInt(input.substring(0));
            blockchain.setZeros(aux);
            zerosSet = true;
            System.out.println("Blockchain inicializada con " + aux + " cero" + (aux == 0 || aux > 1 ? "s" : ""));
            System.out.println();
          } else {
            System.out.println("No se ingreso un numero.\n");
          }

        } else {
          System.out.println("Enter some text, or '" + EXIT_COMMAND + "' to quit");
          System.out.print("> ");

          input = ConsoleReader.readingFromConsole();

          if (input.matches("^(add\\s\\d*)$")) {
            Integer aux = Integer.parseInt(input.substring(4));
            System.out.println("Agregando nodo: " + aux);

            try {
              avlTree.insert(aux);
              System.out.println("Se agrego correctamente el nodo: " + aux);
              System.out.println("Generando hash del bloque, esto puede demorar.");
              blockchain.add(aux, true);
              System.out.println("Hash generado: " + blockchain.getNewBlockHash() + "\n");

            } catch (DuplicateNodeInsertException e) {
              System.out.println("No se pudo agregar, nodo ya existente");
              System.out.println("Generando hash del bloque, esto puede demorar.");
              blockchain.add(aux, false);
              System.out.println("Hash generado: " + blockchain.getNewBlockHash() + "\n");
            }

          } else if (input.matches("^(lookup\\s\\d*)$")) {
            System.out.println("Buscaste un elemento");
            Integer aux = Integer.parseInt(input.substring(7));
            System.out.println("Voy a buscar este nodo: " + aux);

          } else if (input.matches("^(remove\\s\\d*)$")) {
            Integer aux = Integer.parseInt(input.substring(7));
            System.out.println("Borrando nodo: " + aux);

            try {
              avlTree.delete(aux);
              System.out.println("Se eliminó correctamente el nodo: " + aux);
              System.out.println("Generando hash del bloque, esto puede demorar.");
              blockchain.add(aux, true);
              System.out.println("Hash generado: " + blockchain.getNewBlockHash() + "\n");

            } catch (NodeNotFoundException e) {
              System.out.println("No se pudo eliminar, nodo inexistente");
              System.out.println("Generando hash del bloque, esto puede demorar.");
              blockchain.add(aux, false);
              System.out.println("Hash generado: " + blockchain.getNewBlockHash() + "\n");
            }

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
