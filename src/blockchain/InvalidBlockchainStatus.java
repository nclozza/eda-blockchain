package blockchain;

/**
 * This exception is thrown when a method that wants to operate upon the blockchain is operating upon an invalid
 * blockchain.
 */
public class InvalidBlockchainStatus extends Exception {
    public InvalidBlockchainStatus() {}

    public InvalidBlockchainStatus(String message) {
        super(message);
    }
}
