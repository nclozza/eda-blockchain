package blockchain;

public abstract class Operation<T> {

  private String name;
  private T data;
  private boolean status;

  public Operation(String name, T data, boolean status) {
    this.name = name;
    this.data = data;
    this.status = status;
  }

  public String getName() {
    return this.name;
  }

  public T getData() {
    return data;
  }

  public boolean isStatus() {
    return status;
  }

  public String toString() {
    return "Se aplico la operacion " + this.getName() + " al nodo " + this.getData() + ". Estado de la operacion: " + this.isStatus();
  }

  public String toStringForHash() {
    return this.getName() + this.getData() + this.isStatus();
  }

  /**
   * IMPORTANT: This method is only available so we can simulate an unwanted data manipulation
   */
  public void modifyOperation(T data) {
    this.data = data;
  }

  public String oprationClass() {
    return "";
  }
}
