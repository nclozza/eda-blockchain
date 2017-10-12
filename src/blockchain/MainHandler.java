package blockchain;

import visual.BinaryTreeView;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

public class MainHandler {

  private static final String EXIT_COMMAND = "exit";

  private AVLTree<Integer> avlTree;
  private BinaryTreeView<Integer> binaryTreeView;
  private Server<Integer> server;
  private User<Integer> user;

  public MainHandler() {
    this.server = new Server<>();
    this.user = new User<>();
    this.avlTree = new AVLTree<>();
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
          System.out.print("Ingrese la cantidad de ceros: ");
          input = ConsoleReader.readingFromConsole();

          if (input.matches("^\\d*$")) {
            Integer aux = Integer.parseInt(input.substring(0));
            user.setZeros(aux);
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
              LinkedList<Node<Integer>> listOfModifiedNodes = avlTree.insert(aux);
              server.setModifiedNodesByBlock(listOfModifiedNodes, user.getActualBlockNumber());
              server.setAvlTreeState(avlTree.toStringForHash(), avlTree.clone());
              System.out.println("Se agrego correctamente el nodo: " + aux);
              try {

                user.addNewBlock(aux, true);

                blockchain.updateAVL(this.avlTree);
                blockchain.add(aux, true);
//                blockchain.updateAVL(this.avlTree);

                System.out.println("Generando hash del bloque, esto puede demorar.");
                System.out.println("Hash generado: " + user.getNewBlockHash() + "\n");

              } catch (InvalidBlockchainStatus invalidBlockchainStatus) {
                System.out.println("La blockchain es inválida, no se pueden realizar operaciones\n");
              }

            } catch (DuplicateNodeInsertException e) {
              System.out.println("No se pudo agregar, nodo ya existente");
              try {
                user.addNewBlock(aux, false);
                System.out.println("Generando hash del bloque, esto puede demorar.");
                System.out.println("Hash generado: " + user.getNewBlockHash() + "\n");

              } catch (InvalidBlockchainStatus invalidBlockchainStatus) {
                System.out.println("La blockchain es inválida, no se pueden realizar operaciones\n");
              }
            }

          } else if (input.matches("^(lookup\\s\\d*)$")) {
            System.out.println("Buscaste un elemento");
            Integer aux = Integer.parseInt(input.substring(7));
            System.out.println("Buscando nodo: " + aux);
            String auxString = "";
            for (Integer eachBlockNumber : server.getBlockNumbersThatModifiedTheNode(aux)) {
              auxString += eachBlockNumber + " ";
            }
            System.out.println("Números de bloques que modificaron al nodo: " + auxString);

          } else if (input.matches("^(remove\\s\\d*)$")) {
            Integer aux = Integer.parseInt(input.substring(7));
            System.out.println("Borrando nodo: " + aux);

            try {
              avlTree.delete(aux);
              System.out.println("Se eliminó correctamente el nodo: " + aux);
              try {
                user.addNewBlock(aux, true);
                System.out.println("Generando hash del bloque, esto puede demorar.");
                System.out.println("Hash generado: " + user.getNewBlockHash() + "\n");

              } catch (InvalidBlockchainStatus invalidBlockchainStatus) {
                System.out.println("La blockchain es inálida, no se pueden realizar operaciones\n");
              }

            } catch (NodeNotFoundException e) {
              System.out.println("No se pudo eliminar, nodo inexistente");
              try {
                user.addNewBlock(aux, false);
                System.out.println("Generando hash del bloque, esto puede demorar.");
                System.out.println("Hash generado: " + user.getNewBlockHash() + "\n");

              } catch (InvalidBlockchainStatus invalidBlockchainStatus) {
                System.out.println("La blockchain es inválida, no se pueden realizar operaciones\n");
              }
            }

          } else if (input.matches("^(validate)$")) {
            if (user.checkBlockchainStatus()) {
              System.out.println("La blockchain es válida\n");
            } else {
              System.out.println("La blockchain es inválida\n");
            }

          } else if (input.matches("^(modify)$")) {
            System.out.println("Quisiste modificar archivo, todavia no esta listo");

          } else if (input.matches("^(exit)$")) {
            binaryTreeView.closeWindow();
            System.out.println("Nos vemos");
            break;

          } else {
            System.out.println("Horrible input");
          }

          //DELETE LINEA 139 AND 140 AND DECOMMENT LINE 142 WHEN MERGING
          System.out.println(this.avlTree == this.blockchain.getBlockchain().getFirst().getData().getAvlTree());
          binaryTreeView.refresh((Node<Integer>) this.blockchain.getBlockchain().getFirst().getData().getAvlTree().getRoot());

//          binaryTreeView.refresh(avlTree.getRoot());
        }
      }

    } catch (IOException e) {
      System.out.println("Exception has been thrown");
    }
  }
}
