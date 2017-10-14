package blockchain;

/**
 * The Data class represents the data stored in each block. The data consists of an operation and the hash reference
 * to the AVL tree stage after said operation was applied. The actual AVL tree stage is stored in a remote server to
 * reduce the storage of each user of the blockchain.
 */
public class Data<T> {
    private Operation<T> operation;
    /**
     * The reference to the AVL tree stage after the operation was applied to it.
     */
    private String avlTreeHash;

    public Data(Operation<T> operation, String avlTreeHash) {
        this.operation = operation;
        this.avlTreeHash = avlTreeHash;
    }

    public Operation<T> getOperation() {
        return operation;
    }

    public String getAvlTreeHash() {
        return avlTreeHash;
    }

    public String toStringForHash() {
        return this.getOperation().toStringForHash() + this.getAvlTreeHash();
    }
}
