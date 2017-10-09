package blockchain;

import java.util.LinkedList;

public class Blockchain<T extends Comparable<? super T>> {

  private AVLTree<T> avlTree;
  private LinkedList<Block<T>> blockchain;
  private int zeros;

  public Blockchain() {
    this.avlTree = new AVLTree<>();
    this.blockchain = new LinkedList<>();
  }

  public void setZeros(int zeros) {
    this.zeros = zeros;
  }

  public void add(T element, boolean status) {


    Add<T> operation = new Add<>(element, status);
    Data<T> data = new Data<>(operation, this.avlTree);
    String previousBlockHash;
    if (blockchain.size() == 0) {
      previousBlockHash = "0000000000000000000000000000000000000000000000000000000000000000";
    } else {
      previousBlockHash = blockchain.getFirst().getPreviousBlockHash();
    }
    Block<T> block = new Block<>(this.blockchain.size(), data, previousBlockHash, zeros);
    this.blockchain.addFirst(block);
  }

  /**
   * TODO: Please check the method preOrder in AVLTree Class, it's really necessary this kind of implementation?
   */
  public void printAVLTree() {
    this.avlTree.preOrder(this.avlTree.getRoot());
  }


}
