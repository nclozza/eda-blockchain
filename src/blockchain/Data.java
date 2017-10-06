package blockchain;

/**
 * Created by Nico on 5/10/2017.
 */
public class Data<T> {

  private Operation<T> operation;
  private AVLTree<? super T> avlTree;

  public Data(Operation<T> operation, AVLTree<? super T> avlTree) {
    this.operation = operation;
    this.avlTree = avlTree;
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
