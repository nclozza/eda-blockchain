package blockchain;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * The Blockchain class is an implementation of a blockchain used to store operations made upon an AVL tree in every
 * block. One of its properties is the blockchain itself which is a list of blocks. The Block class implementation is
 * set as an inner private class of the blockchain which can be seen below.
 */
public class Blockchain<T extends Comparable<? super T>> {
    /**
     * The hash reference to the previous block of the first block.
     */
    private static String genesisHash = "0000000000000000000000000000000000000000000000000000000000000000";
    /**
     * The blockchain stores a reference to the last state of the AVL tree to let every user have access to it. If a
     * user wants to have access to an earlier stage of the AVL tree they can travel across the blockchain through its
     * blocks, which have the hash to the corresponding state of the AVL tree after the operation that is stored within
     * the block.
     */
    private AVLTree<T> avlTree;
    /**
     * The actual blockchain that consists of a list of blocks.
     */
    private LinkedList<Block<T>> blockchain;
    /**
     * The amount of zeros used for the hash.
     */
    private int zeros;


    public Blockchain() {
        this.avlTree = new AVLTree<>();
        this.blockchain = new LinkedList<>();
    }

    public LinkedList<Block<T>> getBlockchain() {
        return blockchain;
    }

    /**
     * Checks if the hash of the current block that references to the previous block is equal to the hash of said block.
     * @return  True if the hash references of both blocks match, false otherwise.
     */
    public boolean checkBlockchainStatus() {
        if (blockchain.size() == 0 || blockchain.size() == 1) {
            return true;
        }

        Iterator<Block<T>> actual = blockchain.iterator();
        Iterator<Block<T>> previous = blockchain.iterator();
        previous.next();

        do {
            if (!actual.next().getPreviousBlockHash().equals(previous.next().getHash())) {
                return false;
            }
        } while (previous.hasNext());

        return true;
    }

    public void updateAVL(AVLTree<T> avl){
        this.avlTree = avl;
    }

    public void setZeros(int zeros) {
        this.zeros = zeros;
    }

    public String getNewBlockHash() {
        return blockchain.getFirst().getHash();
    }

    /**
     * Indicates the index of the current block.
     * @return  The index of the current block.
     */
    public int getCurrentBlockNumber() {
        return blockchain.size();
    }

    /**
     * Indicates the size of the blockchain which is to say the amount of blocks in it.
     * @return  The amount of blocks in the blockchain.
     */
    public int getBlockchainSize() {
        return blockchain.size();
    }

    /**
     * IMPORTANT: This method is only available so we can simulate an unwanted data manipulation
     */
    public void modifyBlock(int blockNumber, T dataValue) {
        String operation = blockchain.get(blockchain.size() - blockNumber - 1).getData().getOperation().oprationClass();

        Block<T> auxBlock = blockchain.get(blockchain.size() - blockNumber - 1);
        auxBlock.getData().getOperation().modifyOperation(dataValue);

        blockchain.set(blockchain.size() - blockNumber - 1,
                new Block<>(blockchain.size() - blockNumber - 1, auxBlock.getData(), auxBlock.getPreviousBlockHash(),
                        zeros));
    }

    /**
     * Adds a new block to the blockchain. The block will consist of the operation made upon the tree, the element that
     * is passed to the operation method, and the status which indicates if the operation was done successfully.
     * @param element   The element that will be passed to the operation method.
     * @param status    The status of the operation, true if it was done successfully, false otherwise.
     * @param operation The operation to be done upon the tree.
     * @throws InvalidBlockchainStatus if the the references between two blocks do not match (see checkBlockchainStatus
     *         method for further reference)
     */
    public void addNewBlock(T element, boolean status, String operation) throws InvalidBlockchainStatus {
        if (!this.checkBlockchainStatus()) {
            throw new InvalidBlockchainStatus();
        }

        Operation<T> newOperation = new Add<>(element, status);

        if (operation.equals("Remove")) {
            newOperation = new Remove<>(element, status);
        }

        Data<T> data = new Data<>(newOperation, SHA256.getInstance().hash(avlTree.toStringForHash()));

        String previousBlockHash;

        if (blockchain.size() == 0) {
            previousBlockHash = genesisHash;
        } else {
            previousBlockHash = blockchain.getFirst().getHash();
        }

        Block<T> block = new Block<>(blockchain.size(), data, previousBlockHash, zeros);

        blockchain.addFirst(block);
    }

    /**
     * The Block class is the implementation of a block in a blockchain that contains an index, a nonce to set the
     * amount of zeros that will be used to check the hash, data contained in the block, its hash and the hash of the
     * previous block.
     */
    private static class Block<T> {
        private int index;
        private int nonce;
        private Data<T> data;
        private String hash;
        private String previousBlockHash;

        private Block(int index, Data<T> data, String previousBlockHash, int zeros) {
            this.index = index;
            this.data = data;
            this.previousBlockHash = previousBlockHash;
            this.setNonceAndHash(zeros);
        }

        private int getIndex() {
            return index;
        }

        private int getNonce() {
            return nonce;
        }

        private Data<T> getData() {
            return data;
        }

        private String getHash() {
            return hash;
        }

        private String getPreviousBlockHash() {
            return previousBlockHash;
        }

        private String toStringForHash() {
            return index + nonce + data.toStringForHash() + previousBlockHash;
        }

        private void setNonceAndHash(int zeros) {
            String auxHash;
            int auxNonce = -1;

            do {
                auxNonce++;
                auxHash = SHA256.getInstance().hash(index + auxNonce + data.toStringForHash()
                        + previousBlockHash);
            } while (!auxHash.substring(0, zeros).matches("0{" + zeros + "}"));

            this.nonce = auxNonce;
            this.hash = auxHash;
        }

        /**
         * IMPORTANT: This method is only available so we can simulate an unwanted data manipulation
         */
        private void modifyBlock(int zeros) {
            this.setNonceAndHash(zeros);
        }

        /**
         * Two instances of the Block class are equal if all of their parameters except their nonce is equal.
         */
        @Override
        public boolean equals(Object obj) {
          if (obj == null) {
              return false;
          }

          if (!obj.getClass().equals(Block.class)) {
              return false;
          }

          if (!((Block)obj).getHash().equals(this.getHash())) {
              return false;
          }

          if (!((Block)obj).getPreviousBlockHash().equals(this.getPreviousBlockHash())) {
              return false;
          }

          if (((Block)obj).getIndex() != this.getIndex()) {
              return false;
          }

          return ((Block)obj).getData().equals(this.getData());
        }

        @Override
        public int hashCode() {
          return hash.hashCode() + previousBlockHash.hashCode() * 13;
        }
    }
}
