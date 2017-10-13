package blockchain;

import visual.BinaryTreeView;

import java.io.IOException;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The MainHandler class is an implementation suited to handle the entire project consisting of the AVL tree and the
 * blockchain. The main handler's main task is to run the project for a certain user connected to the server.
 */
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
            this.binaryTreeView = new BinaryTreeView<>(avlTree.getRoot(), 500, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The handler's main task, running the project asking the user for input and translating that action to an
     * operation to the AVL tree, the blockchain or both.
     */
    public void runCode() {
        String input;
        boolean zerosSet = false;

        try {
            while (true) {
                if (!zerosSet) {
                    zerosSet = setZeros(zerosSet);
                } else {
                    System.out.println("Enter some text, or '" + EXIT_COMMAND + "' to quit");
                    System.out.print("> ");

                    input = ConsoleReader.readingFromConsole();

                    if (input.matches("^(add\\s\\d+)$")) {
                        add(input);
                    } else if (input.matches("^(remove\\s\\d+)$")) {
                        remove(input);
                    } else if (input.matches("^(lookup\\s\\d+)$")) {
                        lookup(input);
                    } else if (input.matches("^(validate)$")) {
                        validate();
                    } else if (input.matches("^(modify)$")) {
                        Integer blockNumber = -1;
                        Integer dataValue = -1;
                        String auxInput;

                        System.out.print("Ingrese el número de bloque que desea modificar: ");

                        auxInput = ConsoleReader.readingFromConsole();

                        if (auxInput.matches("^\\d+")) {
                            blockNumber = Integer.parseInt(auxInput);

                            System.out.print("Ingrese el valor: ");

                            auxInput = ConsoleReader.readingFromConsole();

                            if (auxInput.matches("^\\d+")) {
                                dataValue = Integer.parseInt(auxInput);

                                    if (blockNumber < user.getBlockchainSize()) {
                                        user.modifyBlock(blockNumber, dataValue);

                                        System.out.println("Modificaste el bloque número " + blockNumber
                                                + " con el valor " + dataValue + ".\n");
                                    } else {
                                        System.out.println("No existe ese número de bloque.\n");
                                    }
                            } else {
                                System.out.println("Error, debe ingresar un número.\n");
                            }
                        } else {
                            System.out.println("Error, debe ingresar un número\n");
                        }
                    } else if (input.matches("^(exit)$")) {
                        binaryTreeView.closeWindow();

                        System.out.println("Hasta luego, vuelva prontos.");

                        break;
                    } else {
                        System.out.println("Error en el input.");
                    }

                    binaryTreeView.refresh(avlTree.getRoot());
                }
            }
        } catch (IOException e) {
            System.out.println("Exception has been thrown.");
        }
    }

    private boolean setZeros(boolean zerosSet) throws IOException {
        System.out.print("Ingrese la cantidad de ceros: ");
        String input = ConsoleReader.readingFromConsole();

        if (input.matches("^\\d+$")) {
            Integer amtOfZeros = Integer.parseInt(input);
            user.setZeros(amtOfZeros);
            zerosSet = true;

            System.out.println("Blockchain inicializada con " + amtOfZeros + " cero"
                    + (amtOfZeros == 0 || amtOfZeros > 1 ? "s\n" : "\n"));
        } else {
            System.out.println("No se ingreso un numero.\n");
        }

        return zerosSet;
    }

    private void add(String input) {
        Integer node = Integer.parseInt(input.substring(4));

        System.out.println("Agregando el nodo: " + node);

        try {
            LinkedList<Node<Integer>> listOfModifiedNodes = avlTree.insert(node);
            server.setModifiedNodesByBlock(listOfModifiedNodes, user.getActualBlockNumber());

            System.out.println("Se agregó correctamente el nodo: " + node);

            user.addNewBlock(node, true, "Add");
            AVLTree<Integer> auxAVLTree = (AVLTree<Integer>) avlTree.clone();
            server.setAvlTreeState(SHA256.getInstance().hash(avlTree.toStringForHash()), auxAVLTree);
            user.updateAVL(avlTree);

            System.out.println("Generando hash del bloque, esto puede demorar.");
            System.out.println("Hash generado: " + user.getNewBlockHash() + "\n");
        } catch (InvalidBlockchainStatus invalidBlockchainStatus) {
            System.out.println("La blockchain es inválida, no se pueden realizar operaciones.\n");
        } catch (CloneNotSupportedException e) {
            System.out.println("No se pudo guardar el nuevo estado del AVL en el servidor.");
        } catch (DuplicateNodeInsertException e) {
            System.out.println("No se pudo agregar, nodo ya existente.");

            try {
                user.addNewBlock(node, false, "Add");

                System.out.println("Generando hash del bloque, esto puede demorar.");
                System.out.println("Hash generado: " + user.getNewBlockHash() + "\n");
            } catch (InvalidBlockchainStatus invalidBlockchainStatus) {
                System.out.println("La blockchain es inválida, no se pueden realizar operaciones.\n");
            }
        }
    }

    private void remove(String input) {
        Integer node = Integer.parseInt(input.substring(7));
        System.out.println("Borrando el nodo: " + node);

        try {
            LinkedList<Node<Integer>> listOfModifiedNodes = avlTree.delete(node);
            server.setModifiedNodesByBlock(listOfModifiedNodes, user.getActualBlockNumber());

            System.out.println("Se eliminó correctamente el nodo: " + node);

            user.addNewBlock(node, true, "Remove");
            AVLTree<Integer> auxAVLTree = (AVLTree<Integer>) avlTree.clone();
            server.setAvlTreeState(SHA256.getInstance().hash(avlTree.toStringForHash()), auxAVLTree);
            user.updateAVL(avlTree);

            System.out.println("Generando hash del bloque, esto puede demorar.");
            System.out.println("Hash generado: " + user.getNewBlockHash() + ".\n");
        } catch (InvalidBlockchainStatus invalidBlockchainStatus) {
            System.out.println("La blockchain es inválida, no se pueden realizar operaciones.\n");
        } catch (CloneNotSupportedException e) {
            System.out.println("No se pudo guardar el nuevo estado del AVL en el servidor.");
        } catch (NodeNotFoundException e) {
            System.out.println("No se pudo eliminar, nodo inexistente.");

            try {
                user.addNewBlock(node, false, "Remove");

                System.out.println("Generando hash del bloque, esto puede demorar.");
                System.out.println("Hash generado: " + user.getNewBlockHash() + ".\n");
            } catch (InvalidBlockchainStatus invalidBlockchainStatus) {
                System.out.println("La blockchain es inválida, no se pueden realizar operaciones.\n");
            }
        }
    }

    private void lookup(String input) {
        Integer node = Integer.parseInt(input.substring(7));

        System.out.println("Buscando el nodo: " + node);

        String blockIndexes = "";
        LinkedList<Integer> blockList = server.getBlockNumbersThatModifiedTheNode(node);

        if (blockList != null) {
            for (Integer eachBlockNumber : blockList) {
                blockIndexes += eachBlockNumber + " ";
            }

            System.out.println("Índices de bloques que modificaron al nodo: " + blockIndexes);
        } else {
            System.out.println("El nodo " + node +  " no fue modificado porque nunca pertenció al árbol.");
        }
    }

    private void validate() {
        if (user.checkBlockchainStatus()) {
            System.out.println("La blockchain es válida.\n");
        } else {
            System.out.println("La blockchain es inválida.\n");
        }
    }

    
}
