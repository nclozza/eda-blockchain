package blockchain;

import java.util.LinkedList;
import java.util.Queue;

/**
 * The AVLTree class is an implementation of an AVL tree with changes made to suit the Blockchain implementation.
 */
public class AVLTree<T extends Comparable<? super T>> {
    /**
     * The header represents the root of the tree.
     */
    private Node<T> header;

    /**
     * The modifiedNodesList is a list containing all the nodes that were modified by the last operation upon the tree.
     * The list will start with a null reference meaning that the tree is empty. It can be accessed by the
     * getModifiedNodesList method.
     */
    private LinkedList<Node<T>> modifiedNodesList = null;


    /**
     * Creates an instance of the AVLTree class which should represent an AVL tree. Said instance has the specified data
     * as the header's value by default. The header works as a general reference for this implementation and as the root
     * node of the AVL tree.
     * @param data  Data to be contained by the header
     */
    public AVLTree(T data) {
        header = new Node<T>(data);
    }

    /**
     * Creates an instance of the AVLTree class which should represent an AVL tree. Said instance has a null header,
     * this means that the tree is empty and there is no root yet.
     */
    public AVLTree() {
        header = null;
    }

    /**
     * Getter method for the root node of the AVL tree.
     * @return  The root node, in this case the header.
     */
    public Node<T> getRoot() {
        return header;
    }

    /**
     * Calculates the height of a given node. A node's height is a private field in the class' definition.
     * @param node Node whose height is to be calculated.
     * @return  The height of the specified node.
     */
    int height(Node<T> node) {
        if (node == null)
            return -1;

        return node.getHeight();
    }

    /**
     * Looks for a node containing the specified data calling the recursive method containsR.
     * @param data  Data to be found contained in a node.
     * @return  True if the node containing the data is found, false otherwise.
     */
    public boolean contains(T data){
        return containsR(data, header);
    }

    /**
     * Looks for a node containing the specified data recursively and returns true if such node could be found, false
     * otherwise.
     * @param data  Data to be found contained in a node.
     * @param node  Node to be use for the search of the specified data.
     * @return True if the node containing the data is found, false otherwise.
     */
    private boolean containsR(T data, Node<T> node) {
        if (node == null) {
            return false;
        } else if (data.compareTo(node.getData()) < 0) {
            return containsR(data, node.getLeft());
        } else if (data.compareTo(node.getData()) > 0) {
            return containsR(data, node.getRight());
        }

        return true;
    }

    /**
     * Inserts a new node with the specified data. Calls a private recursive insert method to look for the proper
     * place to insert the new node.
     * @param data  The data that will be in the new node to be inserted.
     * @return  The list of nodes that were modified by the insertion.
     */
    public LinkedList<Node<T>> insert(T data) throws DuplicateNodeInsertException {
        modifiedNodesList = new LinkedList<>();

        if (header == null) {
            header = new Node<T>(data);

            modifiedNodesList.add(header);
        } else {
            header = insertR(header, data);
        }

        return modifiedNodesList;
    }

    /**
     * Recursive method which looks for the proper place to insert a new node with the specified data in the tree.
     * @param currentNode   Node that works as a reference to know where to insert the new node.
     * @param data  The data that will be in the new node to be inserted.
     * @return  The root/header of the subtree that was affected by the node's insertion.
     * @throws DuplicateNodeInsertException if there is an attempt to insert a new node with the specified data while
     *      already having an existing node with such data in the tree.
     */
    private Node<T> insertR(Node<T> currentNode, T data) throws DuplicateNodeInsertException {

        if (data.compareTo(currentNode.getData()) < 0) { // Has to be inserted in the left subtree.
            if (currentNode.getLeft() == null) {
                Node<T> newNode = new Node<>(data);

                currentNode.setLeft(newNode);

                modifiedNodesList.add(newNode);     // The new leaf is added to the modifiedNodesList list.
                modifiedNodesList.add(currentNode); // Node has a new child, therefore it is also added to modifiedNodesList.
            } else {
                currentNode.setLeft(insertR(currentNode.getLeft(), data));
            }
        } else if (data.compareTo(currentNode.getData()) > 0) { // Has to be inserted in the right subtree.
            if (currentNode.getRight() == null) {
                Node<T> newNode = new Node<>(data);

                currentNode.setRight(newNode);

                modifiedNodesList.add(newNode);     // The new leaf is added to the modifiedNodesList list.
                modifiedNodesList.add(currentNode); // Node has a new child, therefore it is also added to modifiedNodesList.
            } else {
                currentNode.setRight(insertR(currentNode.getRight(), data));
            }
        } else {
            throw new DuplicateNodeInsertException("This AVL tree implementation does not allow for duplicate nodes.");
        }

        // Update the modified currentNodes' heights.
        if ((currentNode.getLeft() == null) && (currentNode.getRight() != null)) {
            currentNode.setHeight(currentNode.getRight().getHeight() + 1);
        } else if ((currentNode.getRight() == null) && (currentNode.getLeft() != null)) {
            currentNode.setHeight(currentNode.getLeft().getHeight() + 1);
        } else {
            currentNode.setHeight(Math.max(height(currentNode.getLeft()), height(currentNode.getRight())) + 1);
        }

        return balanceTree(currentNode);
    }

    /**
     * Delete operation. Calls a private recursive method to delete the node containing the specified data.
     * @param data  Data contained by the node to be deleted.
     * @return  The list of nodes that were modified by the deletion.
     *          only node in the tree and it was deleted.
     */
    public LinkedList<Node<T>> delete(T data) throws NodeNotFoundException {
        modifiedNodesList = new LinkedList<>();

        deleteR(data, header);

        return modifiedNodesList;
    }

    /**
     * Recursive method responsible of finding a node with the specified data and deleting it through the
     * deleteFoundNode() method. After having deleted the node it will set the new height and balance the subtree from
     * where the node was deleted until the root is reached.
     * @param data  The data of the node to be deleted.
     * @param node  The current node whose data is to be compared with the specified data to check if it is to be deleted.
     * @return  A new balanced tree that doesn't contain any nodes with the specified data.
     * @throws NodeNotFoundException if there isn't an existing node containing the specified data.
     */
    private Node<T> deleteR(T data, Node<T> node) throws NodeNotFoundException {
        if (node == null) {
            throw new NodeNotFoundException("The node with the specified data does not exist in the tree.");
        }

        Node<T> leftChild = node.getLeft();
        Node<T> rightChild = node.getRight();
        T currentData = node.getData();

        if (data.compareTo(currentData) < 0) {
            node.setLeft(deleteR(data, leftChild));

            if (node.getLeft() == null || leftChild.getData().compareTo(node.getLeft().getData()) != 0) {
                modifiedNodesList.add(node);
            }
        } else if (data.compareTo(currentData) > 0) {
            node.setRight(deleteR(data, rightChild));

            if (node.getRight() == null || rightChild.getData().compareTo(node.getRight().getData()) != 0) {
                modifiedNodesList.add(node);
            }
        } else {
            node = deleteFoundNode(node);

            if (node == null) {
                return null;
            }
        }

        // Update the height parameter.
        node.setHeight(height(node));

        // Check on every delete operation whether tree has become unbalanced.
        return balanceTree(node);
    }

    /**
     * Performs the actual deletion of the node unlike delete or deleteR which are wrappers. The deletion consists of
     * replacing the given node with a children or the highest valued descendant of its left subtree.
     * @param node  The node to be replaced by one of its children or the highest valued descendant of its left subtree
     *              if it were to have both children.
     * @return  The replacement for the specified node which is to be deleted. The replacement could be null if it had
     *          no children, one if its children if it only had one or the highest valued descendant of its left subtree
     *          if it had both children.
     * @throws NodeNotFoundException if there isn't an existing node containing the specified data, which in this case
     *         won't happen because the node to be deleted is the old copy of the replacement node.
     */
    private Node<T> deleteFoundNode(Node<T> node) throws NodeNotFoundException {
        if (node.getLeft() == null && node.getRight() == null) {
            if (node == header) {
                header = null;
            }

            return null;
        } else if (node.getLeft() == null) {
            if (node == header) {
                header = node.getRight();
            }

            return node.getRight();
        } else if (node.getRight() == null) {
            if (node == header) {
                header = node.getLeft();
            }

            return node.getLeft();
        }

        // In this case the node has two children, the largest descendant in its left subtree is chosen to replace it.
        Node<T> largestInLeftSubtree = getMaxNode(node.getLeft());

        node.setData(largestInLeftSubtree.getData());
        node.setLeft(deleteR(largestInLeftSubtree.getData(), node.getLeft()));

        return node;
    }

    /**
     * Method to get the highest valued node from the subtree of which the specified node is the root/header.
     * @param root  The root from which to start the search for the highest valued node in the subtree.
     * @return  The highest valued node in the subtree.
     */
    private Node<T> getMaxNode(Node<T> root) {
        Node<T> currentNode = root;

        while (currentNode.getRight() != null) {
            currentNode = currentNode.getRight();
        }

        return currentNode;
    }

    /**
     * Calculates the balancing factor of a specified node. The balancing factor of a node is the height difference
     * between its left child and its right child in that order. If the subtree that had the specified node as its
     * root/header was balanced, its balancing factor would be one of these values {-1, 0, 1} depending on its left and
     * right children's heights. If it was not balanced then the value would be an integer different from those
     * specified previously, also depending on its left and right children's heights. If the AVL tree were properly
     * balanced after each insertion or deletion, every node that is not balanced should have -2 or 2 as its balancing
     * factor.
     * @param node  Node whose balancing factor is to be calculated.
     * @return  Balancing factor of the specified node.
     */
    private int getBalance(Node<T> node) {
        if (node == null) {
            return 0;
        }

        return height(node.getLeft()) - height(node.getRight());
    }

    /**
     * Balances the tree leaving a height difference between all leafs of 1 or -1 at most.
     * @param currentNode Header node of the subtree.
     * @return Header node of the balanced subtree.
     */
    private Node<T> balanceTree(Node<T> currentNode) {
        int balanceValue = getBalance(currentNode);

        // Left heavy situation. Can be left-left or left-right.
        if (balanceValue > 1) {
            // Left-right situation. Left rotation on the right child of the root node and the right child itself.
            if (getBalance(currentNode.getLeft()) < 0) {
                currentNode.setLeft(leftRotate(currentNode.getLeft()));
            }

            return rightRotate(currentNode);
        }

        // Right heavy situation. Can be right-right or right-left.
        if (balanceValue < -1) {
            // Right-left situation. Right rotation on the left child of the root node and the left child itself.
            if (getBalance(currentNode.getRight()) > 0) {
                currentNode.setRight(rightRotate(currentNode.getRight()));
            }

            return leftRotate(currentNode);
        }

        return currentNode;
    }

    /**
     * Makes a right rotation upon the subtree of which the specified node is the root/header.
     * @param node  Node which is to be rotated to the right.
     * @return  The new root/header of the previously rotated subtree.
     */
    private Node<T> rightRotate(Node<T> node) {
        Node<T> aux = node.getLeft();

        node.setLeft(aux.getRight());
        aux.setRight(node);

        if (node.getData().compareTo(header.getData()) == 0) {
            header = aux;
        }

        if (!modifiedNodesList.contains(node)) {
            modifiedNodesList.add(node);
        }

        if (!modifiedNodesList.contains(aux)) {
            modifiedNodesList.add(aux);
        }

        // Update the modified nodes' heights.
        node.setHeight(Math.max(height(node.getLeft()), height(node.getRight())) + 1);
        aux.setHeight(Math.max(height(aux.getLeft()), height(aux.getRight())) + 1);

        return aux;
    }

    /**
     * Makes a left rotation upon the subtree of which the specified node is the root/header.
     * @param node  Node which is to be rotated to the left.
     * @return  The new root/header of the previously rotated subtree.
     */
    private Node<T> leftRotate(Node<T> node) {
        Node<T> aux = node.getRight();

        node.setRight(aux.getLeft());
        aux.setLeft(node);

        if (node.getData().compareTo(header.getData()) == 0) {
            header = aux;
        }

        if (!modifiedNodesList.contains(node)) {
            modifiedNodesList.add(node);
        }

        if (!modifiedNodesList.contains(aux)) {
            modifiedNodesList.add(aux);
        }

        // Update the modified nodes' heights.
        node.setHeight(Math.max(height(node.getLeft()), height(node.getRight()) + 1));
        aux.setHeight(Math.max(height(aux.getLeft()), height(aux.getRight()) + 1));

        return aux;
    }

    /**
     * Provides a list containing references to all the nodes that were modified by the last operation upon the tree.
     * @return  The list containing all the nodes that were modified by the last operation upon the tree or null if
     *          there was no operation made upon the tree thus far, which would mean that the tree is empty.
     */
    public LinkedList<Node<T>> getModifiedNodesList() {
        return modifiedNodesList;
    }

    /**
     * Outputs the tree printing it by level. Meaning that the root/header is printed out first, then its children from
     * left to right, then their children from left to right and so on.
     * @param root  The root/header of the subtree that is to be printed by level.
     */
    public void byLevel(Node<T> root) {
        Queue<Node<T>> level = new LinkedList<>();
        level.add(root);

        while (!level.isEmpty()) {
            Node<T> node = level.poll();

            System.out.print(node.getData().toString() + " ");

            if (node.getLeft() != null) {
                level.add(node.getLeft());
            }

            if (node.getRight() != null) {
                level.add(node.getRight());
            }
        }
    }

    /**
     * Method that represents the the entire AVL tree in a String in pre-order traversal. This method serves a
     * wrapper method for the actual recursive method (preOrderR) that travels along the tree to concatenate each
     * node's data.
     * @return  A String of the pre-order traversal of the subtree, each node's content is separated by a space.
     */
    public String preOrder() {
        return preOrderR(header);
    }

    /**
     * Recursive method that represents the subtree that has the specified node as its root/header in a String in
     * pre-order traversal.
     * @param node  The node that is root/header of the subtree that is to be converted into a String in pre-order
     *              traversal.
     * @return  A String of the pre-order traversal of the subtree, each node's content is separated by a space.
     */
    private String preOrderR(Node<T> node) {
        String ret = "";

        if (node != null) {
            ret = ret.concat(node.getData().toString() + " ");
            ret = ret.concat(preOrderR(node.getLeft()));
            ret = ret.concat(preOrderR(node.getRight()));
        }

        return ret;
    }

    /**
     * Method that represents the the entire AVL tree in a String in in-order traversal. This method serves a
     * wrapper method for the actual recursive method (inOrderR) that travels along the tree to concatenate each
     * node's data.
     * @return  A String of the in-order traversal of the subtree, each node's content is separated by a space.
     */
    public String inOrder() {
        return inOrderR(header);
    }

    /**
     * Recursive method that represents the subtree that has the specified node as its root/header in a String in
     * in-order traversal.
     * @param node  The node that is root/header of the subtree that is to be converted into a String in in-order
     *              traversal.
     * @return  A String of the in-order traversal of the subtree, each node's content is separated by a space.
     */
    private String inOrderR(Node<T> node) {
        String ret = "";

        if (node != null) {
            ret = ret.concat(inOrderR(node.getLeft()));
            ret = ret.concat(node.getData().toString() + " ");
            ret = ret.concat(inOrderR(node.getRight()));
        }

        return ret;
    }

    /**
     * Method that represents the the entire AVL tree in a String in post-order traversal. This method serves a
     * wrapper method for the actual recursive method (postOrderR) that travels along the tree to concatenate each
     * node's data.
     * @return  A String of the post-order traversal of the subtree, each node's content is separated by a space.
     */
    public String postOrder() {
        return postOrderR(header);
    }

    /**
     * Recursive method that represents the subtree that has the specified node as its root/header in a String in
     * post-order traversal.
     * @param node  The node that is root/header of the subtree that is to be converted into a String in post-order
     *              traversal.
     * @return  A String of the post-order traversal of the subtree, each node's content is separated by a space.
     */
    private String postOrderR(Node<T> node) {
        String ret = "";

        if (node != null) {
            ret = ret.concat(postOrderR(node.getLeft()));
            ret = ret.concat(node.getData().toString() + " ");
            ret = ret.concat(postOrderR(node.getRight()));
        }

        return ret;
    }

    // THIS IS A TEMPORARY IMPLEMENTATION, WE NEED TO CHANGE THIS
    public String toStringForHash() {
        return this.toString();
    }
}