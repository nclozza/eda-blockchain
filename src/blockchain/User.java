package blockchain;

public class User<T extends Comparable<? super T>> {

  private Blockchain<T> blockchain = new Blockchain<>();

  public boolean checkBlockchainStatus() {
    return blockchain.checkBlockchainStatus();
  }

  public void setZeros(int zeros) {
    blockchain.setZeros(zeros);
  }

  public void addNewBlock(T element, boolean status) throws InvalidBlockchainStatus {
    blockchain.addNewBlock(element, status);
  }

  public String getNewBlockHash() {
    return blockchain.getNewBlockHash();
  }

  public int getActualBlockNumber() {
    return blockchain.getActualBlockNumber();
  }

}
