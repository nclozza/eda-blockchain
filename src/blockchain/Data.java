package blockchain;

public class Data<T> {

  private Operation<T> operation;
  private String avlTreeHash;

  public Data(Operation<T> operation, String avlTreeHash) {
    this.operation = operation;
    this.avlTreeHash = avlTreeHash;
  }

  public Operation<T> getOperation() {
    return operation;
  }

  public String getAvlTreeHash() {
    return avlTreeHash;
  }

  public String toStringForHash() {
    return this.getOperation().toStringForHash() + this.getAvlTreeHash();
  }
}
