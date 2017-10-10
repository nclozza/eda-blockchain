//package blockchain;
//
//import java.util.LinkedList;
//
//public class Blockchain<T extends Comparable<? super T>> {
//
//  private AVLTree<T> avlTree;
//  private LinkedList<Block<T>> blockchain;
//  private int zeros;
//
//  public Blockchain(int zeros) {
//    this.avlTree = new AVLTree<>();
//    this.blockchain = new LinkedList<>();
//    this.zeros = zeros;
//  }
//
//  /**
//   * TODO: The AVLTree method insert must return if the element can be added, now we are hardcoding this value.
//   */
//  public void add(T element) {
//    this.avlTree.insert(element);
//
//    //We suppose that the element its added correctly in the tree
//    Add<T> operation = new Add<>(element, true);
//    Data<T> data = new Data<>(operation, this.avlTree);
//    String previousBlockHash;
//    if (blockchain.size() == 0) {
//      previousBlockHash = "0000000000000000000000000000000000000000000000000000000000000000";
//    } else {
//      previousBlockHash = blockchain.getFirst().getPreviousBlockHash();
//    }
//    Block<T> block = new Block<>(this.blockchain.size(), data, previousBlockHash, zeros);
//    this.blockchain.addFirst(block);
//  }
//
//  /**
//   * TODO: Please check the method preOrder in AVLTree Class, it's really necessary this kind of implementation?
//   */
//  public void printAVLTree() {
//    this.avlTree.preOrder(this.avlTree.getRoot());
//  }
//
//
//}
