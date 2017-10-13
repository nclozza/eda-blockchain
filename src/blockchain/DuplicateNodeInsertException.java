package blockchain;

/**
 * The DuplicateNodeInsertException is thrown whenever a user tries to add content to the AVL tree that is already
 * contained by a node.
 */
public class DuplicateNodeInsertException extends Exception {
    public DuplicateNodeInsertException() {}

    public DuplicateNodeInsertException(String message) {
        super(message);
    }
}
