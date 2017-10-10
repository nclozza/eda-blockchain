package blockchain;

public class DuplicateNodeInsertException extends Exception {
    public DuplicateNodeInsertException() {}

    public DuplicateNodeInsertException(String message) {
        super(message);
    }
}
