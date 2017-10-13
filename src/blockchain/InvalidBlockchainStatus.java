package blockchain;

public class InvalidBlockchainStatus extends Exception {
  public InvalidBlockchainStatus() {
  }

  public InvalidBlockchainStatus(String message) {
    super(message);
  }
}
