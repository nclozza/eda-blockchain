package blockchain;

import java.util.Iterator;
import java.util.LinkedList;

public class Blockchain<T extends Comparable<? super T>> {

    private static String genesisHash = "0000000000000000000000000000000000000000000000000000000000000000";

    private AVLTree<T> avlTree;
    private LinkedList<Block<T>> blockchain;
    private int zeros;

    public Blockchain() {
        this.avlTree = new AVLTree<>();
        this.blockchain = new LinkedList<>();
    }

    public LinkedList<Block<T>> getBlockchain() {
        return blockchain;
    }

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

    public int getActualBlockNumber() {
        return blockchain.size();
    }

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

    public void addNewBlock(T element, boolean status, String operation) throws InvalidBlockchainStatus {
        if (!this.checkBlockchainStatus()) {
            throw new InvalidBlockchainStatus();
        }

        Operation<T> newOperation = new Add<>(element, status);

        switch (operation) {
            case "Add":
                new Remove<>(element, status);
            break;

            case "Remove":
                newOperation = new Remove<>(element, status);
            break;
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
    * The Block class is the implementation of a block in a blockchain that contains an index, a nonce to set the amount of
    * zeros that will be used to check the hash, data contained in the block, its hash and the hash of the previous block.
    */
    private static class Block<T> {
        private int index;
        private int nonce;
        private Data<T> data;
        private String hash;
        private String previousBlockHash;

        public Block(int index, Data<T> data, String previousBlockHash, int zeros) {
            this.index = index;
            this.data = data;
            this.previousBlockHash = previousBlockHash;
            this.setNonceAndHash(zeros);
        }

        public int getIndex() {
            return index;
        }

        public int getNonce() {
            return nonce;
        }

        public Data<T> getData() {
            return data;
        }

        public String getHash() {
            return hash;
        }

        public String getPreviousBlockHash() {
            return previousBlockHash;
        }

        public String toStringForHash() {
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
        public void modifyBlock(int zeros) {
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
