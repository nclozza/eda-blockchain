package blockchain;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class AVLTree<T extends Comparable<? super T>> {

    private Node<T> header;
    private boolean wasRotated = false;
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
     * @param x Element to find
     * @param node Root of the tree
     * @return True if the element is found, false otherwise
     */
    private boolean containsR(T x, Node<T> node) {
        if (node == null){
            return false; // The node was not found
        } else if (x.compareTo(node.getData()) < 0) {
            return containsR(x, node.getLeft());
        } else if (x.compareTo(node.getData()) > 0) {
            return containsR(x, node.getRight());
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
     * @param data Element to delete
     * @param node Root of the tree
     * @return Header after deletion, balanced
     */
    private Node<T> deleteR(T data, Node<T> node) throws NodeNotFoundException {
        if (node == null) {
            throw new NodeNotFoundException("The node with the specified data doesn't exist in the tree.");
        }

        Node<T> leftChild = node.getLeft();
        Node<T> rightChild = node.getRight();
        T currentData = node.getData();

        if (data.compareTo(currentData) == 0) {
            if (leftChild == null && rightChild == null) {
                if (node == header) {
                    header = null;
                }
		return null;

            } else if (leftChild == null) {
                if (node == header) {
                    header = rightChild;
                }

                return header;
            } else if (rightChild == null) {
                if (node == header) {
                    header = leftChild;
                }

                return header;
            } else {
                Node<T> largestInLeftSubtree = getMaxNode(leftChild);
                node.setData(largestInLeftSubtree.getData());
                node.setLeft(deleteR(largestInLeftSubtree.getData(), node.getLeft()));

            }
        } else if (data.compareTo(currentData) < 0) {
            Node<T> auxLeftChild = leftChild;
            node.setLeft(deleteR(data, leftChild));

            if (node.getLeft() == null || auxLeftChild.getData().compareTo(node.getLeft().getData()) != 0) {
                modifiedNodes.add(node);
            }
        } else {
            Node<T> auxRightChild = rightChild;
            node.setRight(deleteR(data, rightChild));

            if (node.getRight() == null || auxRightChild.getData().compareTo(node.getRight().getData()) != 0) {
                modifiedNodes.add(node);
            }
        }

        // Update the height parameter
        node.setHeight(height(node));

        // Check on every delete operation whether tree has become unbalanced
        return balanceTree(node);
    }


    private Node<T> getMaxNode(Node<T> node) {
       return getMaxNodeR(node);
    }

    private Node<T> getMaxNodeR(Node<T> node) {
        if (node.getRight() == null && node.getLeft() != null) {
            return node;
        }

        else if (node.getLeft() == null && node.getRight()!= null) {
            return getMaxNodeR(node.getRight());
        }

        return node;
    }

    /**
     * Balances the new tree after deletion of an element
     * @param currentNode Header
     * @return Tree balanced
     */
    private Node<T> balanceTree(Node<T> currentNode) {
        int balanceValue = getBalance(currentNode);
        // Left heavy situation. Can be left-left or left-right
        if (balanceValue > 1) {
            // Left-right rotation required. Left rotation on the right child of the root node.
            if (getBalance(currentNode.getLeft()) < 0) {
                currentNode.setLeft(leftRotate(currentNode.getLeft()));
            }

            return rightRotate(currentNode);
        }
        // Right heavy situation. Can be right-right or right-left
        if (balanceValue < -1) {
            // right - left situation. Left rotation on the right child of the root node.
            if (getBalance(currentNode.getRight()) > 0) {
                currentNode.setRight(rightRotate(currentNode.getRight()));
            }
            // left rotation on the root node
            return leftRotate(currentNode);
        }
        return currentNode;
    }

    //just to test
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

    //just to test
    public void preOrder(Node<T> node) {
        if (node != null) {
            System.out.print(node.getData().toString() + " ");
            preOrder(node.getLeft());
            preOrder(node.getRight());
        }
    }

    /**
     * Inserts a new node. Calls a private recursive insert function
     * @param data element to insert
     * @return void
     */
    public void insert(T data) throws DuplicateNodeInsertException {
        if (header != null) {
            header = insertR(header, data);
            wasRotated = false;

            return;
        }

        header = new Node<T>(data);

        modifiedNodes.add(header);
    }

    private Node<T> insertR(Node<T> node, T data) throws DuplicateNodeInsertException {
        if (data.compareTo(node.getData()) < 0) {
            if (node.getLeft() == null) {
                Node<T> newNode = new Node(data);

                node.setLeft(newNode);

                modifiedNodes.add(newNode);     // The new leaf is added to the modifiedNodes list
                modifiedNodes.add(node);        // Node has a new child, therefore it is also added to modifiedNodes
            } else {
                node.setLeft(insertR(node.getLeft(), data));
            }
        } else if (data.compareTo(node.getData()) > 0) {
            if (node.getRight() == null) {
                Node<T> newNode = new Node(data);

                node.setRight(newNode);

                modifiedNodes.add(newNode);     // The new leaf is added to the modifiedNodes list
                modifiedNodes.add(node);        // Node has a new child, therefore it is also added to modifiedNodes
            } else {
                node.setRight(insertR(node.getRight(), data));
            }
        } else {
            throw new DuplicateNodeInsertException("This AVL tree implementation doesn't allow for duplicate nodes.");
        }

        if ((node.getLeft() == null) && (node.getRight() != null)) {
            node.setHeight(node.getRight().getHeight() + 1);
        } else if ((node.getRight() == null) && (node.getLeft() != null)) {
            node.setHeight(node.getLeft().getHeight() + 1);
        } else {
            node.setHeight(Math.max(height(node.getLeft()), height(node.getRight())) + 1);
        }

        return balanceTree(node);
    }

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

        node.setHeight(Math.max(height(node.getLeft()), height(node.getRight())) + 1);
        aux.setHeight(Math.max(height(aux.getLeft()), height(aux.getRight())) + 1);

        return aux;
    }

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

        node.setHeight(Math.max(height(node.getLeft()), height(node.getRight()) + 1));
        aux.setHeight(Math.max(height(aux.getLeft()), height(aux.getRight()) + 1));

        return aux;
    }

    int getBalance(Node<T> node) {
        if (node == null) {
            return 0;
        }

        return height(node.getLeft()) - height(node.getRight());
    }

    int height(Node<T> node) {
        if (node == null)
            return -1;

        return node.getHeight();
    }
}