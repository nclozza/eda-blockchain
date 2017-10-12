package blockchain;

/**
 * The NodeNotFoundException is thrown whenever a user tries to delete or search for an unexisting node in the tree.
 */
public class NodeNotFoundException extends Exception {
    public NodeNotFoundException() {}

    public NodeNotFoundException(String message) {
        super(message);
    }
}
