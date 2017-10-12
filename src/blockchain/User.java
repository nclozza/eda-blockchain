package blockchain;

public class User<T extends Comparable<? super T>> {

  private Blockchain<T> blockchain = new Blockchain<>();

  public boolean checkBlockchainStatus() {
    return blockchain.checkBlockchainStatus();
  }

  public void setZeros(int zeros) {
    blockchain.setZeros(zeros);
  }

  public void addNewBlock(T element, boolean status, String operation) throws InvalidBlockchainStatus {
    blockchain.addNewBlock(element, status, operation);
  }

  public String getNewBlockHash() {
    return blockchain.getNewBlockHash();
  }

  public int getActualBlockNumber() {
    return blockchain.getActualBlockNumber();
  }

  public void updateAVL(AVLTree<T> avlTree) {
    blockchain.updateAVL(avlTree);
  }

  public int getBlockchainSize() {
    return blockchain.getBlockchainSize();
  }

  /**
   * IMPORTANT: This method is only available so we can simulate an unwanted data manipulation
   */
  public void modifyBlock(int blockNumber, T dataValue) {
    blockchain.modifyBlock(blockNumber, dataValue);
  }

}
