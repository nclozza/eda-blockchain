package blockchain;

/**
 * The Node class is a representation of a node that has data stored in it and a reference to its right and left
 * children. There is also the height variable to store the height of the node in a tree instead of travelling through
 * the tree to obtain it. This class is the one used for the AVLTree implementation. It is not set as an inside class
 * of the AVL tree because it is used in the graphical interface implementation.
 */
public class Node<T> {
    private T data;
    private int height;
    private Node<T> right = null;
    private Node<T> left = null;

    /**
     * The constructor of a node cannot be called without specifying what data the node will store.
     * @param data  The data to be stored in the node.
     */
    public Node(T data) {
        this.data = data;
        this.height = 0;
    }

    public T getData() {
        return this.data;
    }

    public int getHeight() {
        return this.height;
    }

    public Node<T> getRight() {
        return this.right;
    }

    public Node<T> getLeft() {
        return this.left;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setRight(Node<T> node) {
        this.right = node;
    }

    public void setLeft(Node<T> node) {
        this.left = node;
    }

    /**
     * A Node instance will be equal to another if their data stored is the same.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!obj.getClass().equals(Node.class)) {
            return false;
        }

        if (obj.hashCode() != this.hashCode()) {
            return false;
        }

        return ((Node)obj).getData().equals(this.data);
    }

    @Override
    public int hashCode() {
        return 53 * data.toString().hashCode();
    }
}
