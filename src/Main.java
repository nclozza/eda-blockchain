import blockchain.AVLTree;

public class Main {

    public static void main(String[] args) {
	    AVLTree<Integer> tree = new AVLTree<>(5);
	    tree.addNode(6);
	    tree.addNode(7);
	    tree.addNode(8);
	    tree.addNode(9);
	    tree.addNode(10);
	    tree.addLeft(4);
	    tree.addLeft(3);
	    tree.addLeft(2);
		System.out.println("Max level: " + tree.getMaxLevel());
	    System.out.println(tree.getHeight());
	    System.out.println(tree.isBalanced());
    }
}
