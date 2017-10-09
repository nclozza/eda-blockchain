package blockchain;

public class Node<T> {
    private T data;
    private int height;
    private Node<T> right = null;
    private Node<T> left = null;

    public Node(T data) {
        this.data = data;
        this.height = 0;
    }

    public int getHeight() {
        return this != null?this.height:0;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Node<T> getRight() {
        return this.right;
    }

    public Node<T> getLeft() {
        return this.left;
    }

    public void setRight(Node<T> node) {this.right = node;
    }

    public void setLeft(Node<T> node) {
        this.left = node;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
