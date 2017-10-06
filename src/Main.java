import blockchain.Blockchain;

public class Main {

  public static void main(String[] args) {

    Blockchain<Integer> blockchain = new Blockchain<>(4);

    blockchain.add(4);
    blockchain.add(5);

    blockchain.printAVLTree();

  }


}
