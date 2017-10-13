package blockchain;

/**
 * The user class simulates a user of the blockchain implementation. Instead of having to download all of the AVL tree's
 * stages, the user will store only a hard copy of the last stage of the tree while having the hash references to all
 * the previous stages in each block of the blockchain.
 */
public class User<T extends Comparable<? super T>> {
    private Blockchain<T> blockchain = new Blockchain<>();


    public boolean checkBlockchainStatus() {
        return blockchain.checkBlockchainStatus();
    }

    public void setZeros(int zeros) {
        blockchain.setZeros(zeros);
    }

    public void addNewBlock(T element, boolean status, String operation) {
        blockchain.addNewBlock(element, status, operation);
    }

    public String getNewBlockHash() {
        return blockchain.getNewBlockHash();
    }

    public int getActualBlockNumber() {
        return blockchain.getCurrentBlockNumber();
    }

    public void updateAVL(AVLTree<T> avlTree) {
        blockchain.updateAVL(avlTree);
    }

    public int getBlockchainSize() {
        return blockchain.getBlockchainSize();
    }

    /**
    * IMPORTANT: This method is only available so we can simulate an unwanted data manipulation
    */
    public void modifyBlock(int blockNumber, T dataValue) {
        blockchain.modifyBlock(blockNumber, dataValue);
    }

}
