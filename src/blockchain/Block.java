package blockchain;

public class Block<T> {

  private int index;
  private int nonce;
  private Data<T> data;
  private String hash;
  private String previousBlockHash;

  public int getIndex() {
    return index;
  }

  public int getNonce() {
    return nonce;
  }

  public Data<T> getData() {
    return data;
  }

  public String getHash() {
    return hash;
  }

  public String getPreviousBlockHash() {
    return previousBlockHash;
  }

  public String toStringForHash() {
    return index + nonce + data.toStringForHash() + previousBlockHash;
  }

  public Block(int index, Data<T> data, String previousBlockHash, int zeros) {
    this.index = index;
    this.data = data;
    this.previousBlockHash = previousBlockHash;
    this.setNonceAndHash(zeros);
  }

  private void setNonceAndHash(int zeros) {
    String auxHash;
    int auxNonce = -1;
    do {
      auxNonce++;
      auxHash = SHA256.getInstance().hash(index + auxNonce + data.toStringForHash() + previousBlockHash);
    } while (!auxHash.substring(0, zeros).matches("0{" + zeros + "}"));

    this.nonce = auxNonce;
    this.hash = auxHash;
  }
}
