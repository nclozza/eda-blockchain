package blockchain;

import java.util.Iterator;
import java.util.LinkedList;

public class Blockchain<T extends Comparable<? super T>> {

  private static String genesisHash = "0000000000000000000000000000000000000000000000000000000000000000";

  private AVLTree<T> avlTree;
  private LinkedList<Block<T>> blockchain;
  private int zeros;

  public Blockchain() {
    this.avlTree = new AVLTree<>();
    this.blockchain = new LinkedList<>();
  }

  public LinkedList<Block<T>> getBlockchain() {
    return blockchain;
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

  public void updateAVL(AVLTree<T> avl){
    this.avlTree = avl;
  }

  public void setZeros(int zeros) {
    this.zeros = zeros;
  }

  public String getNewBlockHash() {
    return blockchain.getFirst().getHash();
  }

  public int getActualBlockNumber() {
    return blockchain.size();
  }

  public int getBlockchainSize() {
    return blockchain.size();
  }

  /**
   * IMPORTANT: This method is only available so we can simulate an unwanted data manipulation
   */
  public void modifyBlock(int blockNumber, T dataValue) {
    String operation = blockchain.get(blockchain.size() - blockNumber - 1).getData().getOperation().oprationClass();

    Block<T> auxBlock = blockchain.get(blockchain.size() - blockNumber - 1);
    auxBlock.getData().getOperation().modifyOperation(dataValue);

    blockchain.set(blockchain.size() - blockNumber - 1, new Block<>(blockchain.size() - blockNumber - 1, auxBlock.getData(), auxBlock.getPreviousBlockHash(), zeros));
  }

  public void addNewBlock(T element, boolean status, String operation) throws InvalidBlockchainStatus {
    if (!this.checkBlockchainStatus()) {
      throw new InvalidBlockchainStatus();
    }
    Operation<T> newOperation = new Add<>(element, status);
    switch (operation) {
      case "Add":
        break;

      case "Remove":
        newOperation = new Remove<>(element, status);
        break;

      case "Lookup":
        newOperation = new Lookup<>(element, status);
        break;
    }

    Data<T> data = new Data<>(newOperation, SHA256.getInstance().hash(avlTree.toStringForHash()));
    String previousBlockHash;
    if (blockchain.size() == 0) {
      previousBlockHash = genesisHash;
    } else {
      previousBlockHash = blockchain.getFirst().getHash();
    }
    Block<T> block = new Block<>(blockchain.size(), data, previousBlockHash, zeros);
    blockchain.addFirst(block);
  }

}
