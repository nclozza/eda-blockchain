package blockchain;

public class Add<T> extends Operation {

  public Add(T data, boolean status) {
    super("Add", data, status);
  }

  public String oprationClass() {
    return "Add";
  }
}
