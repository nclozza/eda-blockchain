package blockchain;

import java.util.HashMap;
import java.util.LinkedList;

public class Server<T extends Comparable<? super T>> {

  HashMap<String, AVLTree<T>> avlTreeStates = new HashMap<>();
  HashMap<T, LinkedList<Integer>> modifiedNodesByBlocks = new HashMap<>();

  public AVLTree<T> getAVLTreeState(String avlTreeHash) {
    return avlTreeStates.get(avlTreeHash);
  }

  public void setAvlTreeState(String avlTreeHash, AVLTree<T> avlTreeState) {
    avlTreeStates.put(avlTreeHash, avlTreeState);
  }

  public LinkedList<Integer> getBlockNumbersThatModifiedTheNode(T node) {
    return modifiedNodesByBlocks.get(node);
  }

  public void setModifiedNodesByBlock(LinkedList<T> modifiedNodes, Integer blockNumber) {
    for(T eachModifiedNode : modifiedNodes) {
      modifiedNodesByBlocks.get(eachModifiedNode).add(blockNumber);
    }
  }
}
