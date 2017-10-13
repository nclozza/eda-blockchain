package blockchain;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * The Server class represents a remote server
 */
public class Server<T extends Comparable<? super T>> {
    private HashMap<String, AVLTree<T>> avlTreeStates = new HashMap<>();
    private HashMap<T, LinkedList<Integer>> modifiedNodesByBlocks = new HashMap<>();


    public AVLTree<T> getAVLTreeState(String avlTreeHash) {
        return avlTreeStates.get(avlTreeHash);
    }

    public void setAvlTreeState(String avlTreeHash, AVLTree<T> avlTreeState) {
        avlTreeStates.put(avlTreeHash, avlTreeState);
    }

    public LinkedList<Integer> getBlockNumbersThatModifiedTheNode(T node) {
        return modifiedNodesByBlocks.get(node);
    }

    public void setModifiedNodesByBlock(LinkedList<Node<T>> modifiedNodes, Integer blockNumber) {
        for(Node<T> eachModifiedNode : modifiedNodes) {
            if (modifiedNodesByBlocks.get(eachModifiedNode.getData()) == null) {
                LinkedList<Integer> auxList = new LinkedList<>();
                auxList.add(blockNumber);
                modifiedNodesByBlocks.put(eachModifiedNode.getData(), auxList);
            } else {
                modifiedNodesByBlocks.get(eachModifiedNode.getData()).add(blockNumber);
            }
        }
    }
}
