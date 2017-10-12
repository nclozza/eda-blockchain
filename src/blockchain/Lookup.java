package blockchain;

public class Lookup<T> extends Operation {

  public Lookup(T data, boolean status) {
    super("Lookup", data, status);
  }

  public String oprationClass() {
    return "Lookup";
  }
}
