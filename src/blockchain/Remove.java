package blockchain;

public class Remove<T> extends Operation {

  public Remove(T data, boolean status) {
    super("Remove", data, status);
  }
}
