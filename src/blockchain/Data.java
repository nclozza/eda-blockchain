package blockchain;

public class Data<T> {

  private Operation<T> operation;
  private AVLTree<? super T> avlTree;

  public Data(Operation<T> operation, AVLTree<? super T> avlTree) {
    this.operation = operation;
    try {
      this.avlTree = (AVLTree<? super T>) avlTree.clone();
    }catch (CloneNotSupportedException e){
      System.out.println("CLONE NOT SUPPORTED");
    }
  }

  public Operation<T> getOperation() {
    return operation;
  }

  public AVLTree<? super T> getAvlTree() {
    return avlTree;
  }

  public String toStringForHash() {
    return this.getOperation().toStringForHash() + this.getAvlTree().toStringForHash();
  }
}
