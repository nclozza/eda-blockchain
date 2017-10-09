import blockchain.AVLTree;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import blockchain.ConsoleReader;
import blockchain.MainHandler;
import visual.BinaryTreeView;

public class Main {

  public static void main(String[] args) throws IOException {

    MainHandler mainHandler = new MainHandler(4);
    mainHandler.runCode();
  }
}
