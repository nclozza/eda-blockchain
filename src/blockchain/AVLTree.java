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
     * Getter function for the root node of the AVL tree.
     * @return  The root node, in this case the header.
     */
    public Node<T> getRoot() {
        return header;
    }

    // THIS IS A TEMPORARY IMPLEMENTATION, WE NEED TO CHANGE THIS
    public String toStringForHash() {
        return this.toString();
    }

    /**
     * Looks for a node containing the specified data calling the recursive function containsR.
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
     * Looks for a node containing the specified data and returns a list of all the block's index that modified such
     * node. It is understood by "modification" the insertion of the node, the rotation of the node or the replacement
     * of one or both of the node's children by any certain operation. The function calls for a more specific
     * recursive function lookUpR for an easier search of the node containing the specified data.
     * @param data  The data contained by the node whose list of block's index that modified it is asked for.
     * @return  A list with all the block's index that modified the node containing the specified data.
     */
    public LinkedList<Integer> lookUp(T data){
        return lookUpR(data, header);
    }

    /**
     * Recursive function called by lookUp that serves the same purpose as its caller.
     * @param data  The data contained by the node whose list of block's index that modified it is asked for.
     * @param node  The node to be used for a recursive search of the node containing the specified data.
     * @return  A list with all the block's index that modified the node containing the specified data.
     */
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
     * Delete operation. Calls a private recursive method to delete the node containing the specified data.
     * @param data  Data contained by the node to be deleted.
     * @return  True if there is still a tree with a root/header after the deletion. False if the header/root was the
     *          only node in the tree and it was deleted.
     */
    public boolean delete(T data) throws NodeNotFoundException {
        return deleteR(data, header) != null;
    }

    /**
     * Recursive function responsible of finding a node with the specified data and deleting it through the
     * deleteFoundNode() function. After having deleted the node it will set the new height and balance the subtree from
     * where the node was deleted until the root is reached.
     * @param data  The data of the node to be deleted.
     * @param node  The current node whose data is to be compared with the specified data to check if it is to be deleted.
     * @return  A new balanced tree that doesn't contain any nodes with the specified data.
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
            node.setLeft(deleteR(data, leftChild));

            if (node.getLeft() == null || leftChild.getData().compareTo(node.getLeft().getData()) != 0) {
                modifiedNodes.add(node);
            }
        } else if (data.compareTo(currentData) > 0) {
            node.setRight(deleteR(data, rightChild));

            if (node.getRight() == null || rightChild.getData().compareTo(node.getRight().getData()) != 0) {
                modifiedNodes.add(node);
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
        if (header == null) {
            header = new Node<T>(data);

            modifiedNodes.add(header);
        } else {
            header = insertR(header, data);
        }
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
     * between its left child and its right child in that order. If the subtree that had the specified node as its
     * root/header was balanced, its balancing factor would be one of these values {-1, 0, 1} depending on its left and
     * right children's heights. If it was not balanced then the value would be an integer different from those
     * specified previously, also depending on its left and right children's heights. If the AVL tree were properly
     * balanced after each insertion or deletion, every node that is not balanced should have -2 or 2 as its balancing
     * factor.
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