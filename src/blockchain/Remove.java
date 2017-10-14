package blockchain;

/**
 * The Remove class represents the operation of using the remove method upon the AVL tree.
 */
public class Remove<T> extends Operation {
    public Remove(T data, boolean status) {
        super("Remove", data, status);
    }

    public String operationClass() {
        return "Remove";
    }
}
