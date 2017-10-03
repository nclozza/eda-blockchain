package blockchain;

import blockchain.AVLTree.Node;

public interface BSTinterface<T> {

    public boolean addNode(T data);
    public int getHeight();
    public boolean isEmpty();
    public int size(); //No tan necesario
    public Node<T> search(T data);
    public boolean add();
    public Node<T> getSuccessor();
    public boolean delete(T data);

}
