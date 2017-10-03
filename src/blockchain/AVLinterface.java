package blockchain;

import blockchain.AVLTree.Node;

public interface AVLinterface<T> {

    public boolean isBalanced();
    Node<T> rotateWithLeftChild();
    Node<T> doubleWithLeftChild();
    Node<T> rottaWithRightChild();
    Node<T> doubleWithRightChild();
    public void remove(T data);
    public boolean insert(T data);
}
