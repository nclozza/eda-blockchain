package blockchain;

/**
 * The Block class is the implementation of a block in a blockchain that contains an index, a nonce to set the amount of
 * zeros that will be used to check the hash, data contained in the block, its hash and the hash of the previous block.
 */
public class Block<T> {
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
            auxHash = SHA256.getInstance().hash(index + auxNonce + data.toStringForHash() + previousBlockHash);
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
