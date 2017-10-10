package blockchain;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class Blockchain<T extends Comparable<? super T>> {

  private static String genesisHash = "0000000000000000000000000000000000000000000000000000000000000000";

  private AVLTree<T> avlTree;
  private LinkedList<Block<T>> blockchain;
  private int zeros;

  public Blockchain() {
    this.avlTree = new AVLTree<>();
    this.blockchain = new LinkedList<>();
  }

  public boolean checkBlockchainStatus() {
    if (blockchain.size() == 0 || blockchain.size() == 1) {
      return true;
    }
    Iterator<Block<T>> actual = blockchain.iterator();
    Iterator<Block<T>> previous = blockchain.iterator();
    previous.next();

    do {
      if (!actual.next().getPreviousBlockHash().equals(previous.next().getHash())) {
        return false;
      }
    } while (previous.hasNext());

    return true;
  }

  public void setZeros(int zeros) {
    this.zeros = zeros;
  }

  public String getNewBlockHash() {
    return blockchain.getFirst().getHash();
  }

  public void add(T element, boolean status) throws InvalidBlockchainStatus {

    if (!this.checkBlockchainStatus()) {
      throw new InvalidBlockchainStatus();
    }

    Add<T> operation = new Add<>(element, status);
    Data<T> data = new Data<>(operation, this.avlTree);
    String previousBlockHash;
    if (blockchain.size() == 0) {
      previousBlockHash = genesisHash;
    } else {
      previousBlockHash = blockchain.getFirst().getHash();
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
