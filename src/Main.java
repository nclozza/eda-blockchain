import blockchain.AVLTree;
import java.util.LinkedList;
import java.util.Queue;

public class Main {

    public static void main(String[] args) {
	    AVLTree tree = new AVLTree<>();

//		System.out.println("Max level: " + tree.getMaxLevel());
//	    System.out.println(tree.getHeight());
//	    System.out.println(tree.isBalanced());


			tree.insert(10);
			tree.insert(20);
			tree.insert(30);
			tree.insert(44);
			tree.insert(50);
			tree.insert(25);
			tree.delete(30);

//		System.out.print("Printing balance: ");
//		tree.printBalance();
		System.out.println("");
		tree.preOrder(tree.getRoot());


    }


}
