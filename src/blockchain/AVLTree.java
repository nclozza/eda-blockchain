package blockchain;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class AVLTree<T extends Comparable<? super T>> {

    private Node<T> header;
    private HashMap<T, LinkedList<Integer>> modifiedFields = null;
    /* Key = T = We use the data of the node as a key, if some operation modifies it, we add
    the block's index as a value, so when we do lookUp method we just need to return
    modifiedFields.get(key).
    ITS NOT IMPLEMENTED AS FUNCTIONAL YET
     */
    private LinkedList<Node<T>> modifiedNodes = new LinkedList<>();

    public AVLTree(T data) {
        header = new Node<T>(data);
    }

    public AVLTree() {
        header = null;
    }

    public Node<T> getRoot() {
        return header;
    }

    /**
     * This is a temporary implementation, we need to change this
     */
    public String toStringForHash() {
        return this.toString();
    }

    public boolean contains(T x){
        return containsR(x, header);
    }

    /**
     *
     * @param data Element to find
     * @param node Root of the tree
     * @return True if the element is found, false otherwise
     */
    private boolean containsR(T data, Node<T> node) {
        if (node == null) {
            return false; // The node was not found
        } else if (data.compareTo(node.getData()) < 0) {
            return containsR(data, node.getLeft());
        } else if (data.compareTo(node.getData()) > 0) {
            return containsR(data, node.getRight());
        }

        return true; // Can only reach here if node was found
    }

    public LinkedList<Integer> lookUp(T data){
        return lookUpR(data, header);
    }

    private LinkedList<Integer> lookUpR(T data, Node<T> node) {
        if (node == null) {
            return null;
        }

        if (data.compareTo(node.getData()) < 0){
            return lookUpR(data, node.getLeft());
        } else if (data.compareTo(node.getData()) > 0){
            return lookUpR(data, node.getRight());
        }

        return modifiedFields.get(data);
    }

    /**
     * Delete operation. Calls a private recursive method
     * @param data Element to delete
     * @return Null if a leaf has been deleted
     *         Balance of new tree otherwise
     */
    public boolean delete(T data) throws NodeNotFoundException {
        return deleteR(data, header) != null;
    }

    /**
     *
     * @param data
     * @param node
     * @return
     * @throws NodeNotFoundException if there isn't an existing node containing the specified data.
     */
    private Node<T> deleteR(T data, Node<T> node) throws NodeNotFoundException {
        if (node == null) {
            throw new NodeNotFoundException("The node with the specified data doesn't exist in the tree.");
        }

        Node<T> leftChild = node.getLeft();
        Node<T> rightChild = node.getRight();
        T currentData = node.getData();

        if (data.compareTo(currentData) < 0) {
            if (node.getLeft() == null || leftChild.getData().compareTo(node.getLeft().getData()) != 0) {
                modifiedNodes.add(node);
            }

            node.setLeft(deleteR(data, leftChild));
        } else if (data.compareTo(currentData) > 0) {
            if (node.getRight() == null || rightChild.getData().compareTo(node.getRight().getData()) != 0) {
                modifiedNodes.add(node);
            }

            node.setRight(deleteR(data, rightChild));
        } else {
            node = deleteFoundNode(node);
        }

        // Update the height parameter.
        node.setHeight(height(node));

        // Check on every delete operation whether tree has become unbalanced.
        return balanceTree(node);
    }

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

        // In this case the node has two children, the largest one in its left subtree is chosen to replace it.
        Node<T> largestInLeftSubtree = getMaxNode(node.getLeft());

        node.setData(largestInLeftSubtree.getData());
        node.setLeft(deleteR(largestInLeftSubtree.getData(), node.getLeft()));
    }

    /**
     * Function to get the highest valued node from the subtree of which the specified node is the root/header.
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
     * Outputs the tree printing it by level. Meaning that the root/header is printed out first, then its children from
     * left to right, then their children from left to right and so on.
     * @param root  The root/header of the subtree that is to be printed by level.
     */
    public void byLevel(Node<T> root) {
        Queue<Node> level = new LinkedList<>();
        level.add(root);

        while (!level.isEmpty()) {
            Node node = level.poll();

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
     * Prints the tree in pre-order traversal.
     * @param node  The node whose content is to be printed in the context of the pre-order traversal.
     */
    public void preOrder(Node<T> node) {
        if (node != null) {
            System.out.print(node.getData().toString() + " ");
            preOrder(node.getLeft());
            preOrder(node.getRight());
        }
    }

    /**
     * Prints the tree in in-order traversal.
     * @param node  The node whose content is to be printed in the context of the in-order traversal.
     */
    public void inOrder(Node<T> node) {
        if (node != null) {
            inOrder(node.getLeft());
            System.out.println(node.getData().toString() + " ");
            inOrder(node.getRight());
        }
    }

    /**
     * Prints the tree in post-order traversal.
     * @param node  The node whose content is to be printed in the context of the post-order traversal.
     */
    public void postOrder(Node<T> node) {
        if (node != null) {
            inOrder(node.getLeft());
            inOrder(node.getRight());
            System.out.println(node.getData().toString() + " ");
        }
    }

    /**
     * Inserts a new node with the specified data. Calls a private recursive insert function to look for the proper
     * place to insert the new node.
     * @param data  The data that will be in the new node to be inserted.
     */
    public void insert(T data) throws DuplicateNodeInsertException {
        if (header != null) {
            header = insertR(header, data);

            return;
        }

        header = new Node<T>(data);

        modifiedNodes.add(header);
    }

    /**
     * Recursive function which looks for the proper place to insert a new node with the specified data in the tree.
     * @param currentNode   Node that works as a reference to know where to insert the new node.
     * @param data  The data that will be in the new node to be inserted.
     * @return  The root/header of the subtree that was affected by the node's insertion.
     * @throws DuplicateNodeInsertException if there is an attempt to insert a new node with the specified data while
     *      already having an existing node with such data in the tree.
     */
    private Node<T> insertR(Node<T> currentNode, T data) throws DuplicateNodeInsertException {
        if (data.compareTo(currentNode.getData()) < 0) {
            if (currentNode.getLeft() == null) {
                Node<T> newNode = new Node(data);

                currentNode.setLeft(newNode);

                modifiedNodes.add(newNode);     // The new leaf is added to the modifiedNodes list
                modifiedNodes.add(currentNode);        // Node has a new child, therefore it is also added to modifiedNodes
            } else {
                currentNode.setLeft(insertR(currentNode.getLeft(), data));
            }
        } else if (data.compareTo(currentNode.getData()) > 0) {
            if (currentNode.getRight() == null) {
                Node<T> newNode = new Node(data);

                currentNode.setRight(newNode);

                modifiedNodes.add(newNode);     // The new leaf is added to the modifiedNodes list
                modifiedNodes.add(currentNode);        // Node has a new child, therefore it is also added to modifiedNodes
            } else {
                currentNode.setRight(insertR(currentNode.getRight(), data));
            }
        } else {
            throw new DuplicateNodeInsertException("This AVL tree implementation doesn't allow for duplicate" +
                    "currentNodes.");
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

        if (!modifiedNodes.contains(node)) {
            modifiedNodes.add(node);
        }

        if (!modifiedNodes.contains(aux)) {
            modifiedNodes.add(aux);
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

        if (!modifiedNodes.contains(node)) {
            modifiedNodes.add(node);
        }

        if (!modifiedNodes.contains(aux)) {
            modifiedNodes.add(aux);
        }

        // Update the modified nodes' heights.
        node.setHeight(Math.max(height(node.getLeft()), height(node.getRight()) + 1));
        aux.setHeight(Math.max(height(aux.getLeft()), height(aux.getRight()) + 1));

        return aux;
    }

    /**
     * Calculates the balancing factor of a specified node. The balancing factor of a node is the height difference
     * between its left child and its right child in that order.
     * @param node  Node whose balancing factor is to be calculated.
     * @return  Balancing factor of the specified node.
     */
    int getBalance(Node<T> node) {
        if (node == null) {
            return 0;
        }

        return height(node.getLeft()) - height(node.getRight());
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
}