package blockchain;


import javax.naming.NameNotFoundException;
import java.util.LinkedList;
import java.util.Queue;

public class AVLTree<T extends Comparable<? super T>>{

    private Node<T> header;

    public AVLTree(T data){
        header = new Node<T>(data);
    }

    public AVLTree(){
        header = null;
    }

    public Node<T> getRoot(){
        return header;
    }

//    public Node<T> find(T data){
//        return findR(data, header);
//    }
//
//    private Node<T> findR(T data, Node<T> header) {
//    }

    public boolean delete(T data){
        return deleteR(data, header) != null;
    }

    private Node<T> deleteR(T data, Node<T> node) {
        if (node == null) return null;
        Node<T> leftChild = node.getLeft();
        Node<T> rightChild = node.getRight();
        T currentData = node.getData();

        if (data.compareTo(currentData) == 0) {

            System.out.println("Found the data that we want to remove: " + currentData);

            if (leftChild == null && rightChild == null) {
                System.out.println("Removing a leaf node");
                return null;
            } else if (leftChild == null) {
                System.out.println("Removing a node with a right child");
                node = null;
                return rightChild;
            } else if (rightChild == null) {
                System.out.println("Removing a node with a left child");
                node = null;
                return leftChild;
            } else {
                System.out.println("Removing a node with two children");
                // Find the largest node on the left sub-tree
                Node<T> largestInLeftSubtree = getMaxNode(leftChild);

                // Swap the root node with the largest in left sub-tree
                node.setData(largestInLeftSubtree.getData());
                // Set left-child recursively. Remove the copy left of the largest left child
                node.setLeft(deleteR(largestInLeftSubtree.getData(),node.getLeft()));

            }
        } else if (data.compareTo(currentData) < 0) {
            System.out.println("Traversing to the left ---");
            node.setLeft(deleteR(data, leftChild));
        } else {
            System.out.println("Traversing to the right ---");
            node.setRight(deleteR(data, rightChild));
        }

        // Update the height parameter
        node.setHeight(height(node));

        // Check on every delete operation whether tree has become unbalanced
        return balanceTreeAfterDeletion(node);
    }

    private Node<T> getMaxNode(Node<T> node) {
        if ( height(node.getLeft()) > height(node.getRight())){
            return node.getLeft();
        }
        return node.getRight();
    }

    private Node<T> balanceTreeAfterDeletion(Node<T> currentNode) {
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
    public void byLevel(Node<T> root){
        Queue<Node> level  = new LinkedList<>();
        level.add(root);
        while(!level.isEmpty()){
            Node node = level.poll();
            System.out.print(node.getData().toString() + " ");
            if(node.left!= null)
                level.add(node.left);
            if(node.right!= null)
                level.add(node.right);
        }
    }
    //just to test
    public void preOrder(Node<T> node) {
        if (node != null) {
            System.out.print(node.getData().toString() + " ");
            preOrder(node.left);
            preOrder(node.right);
        }
    }

    public void insert(T data) {
        if (header != null) {
            header = insertR(header, data);
            return;
        }
        header = new Node<T>(data);
        return;
    }

    private Node<T> insertR(Node<T> node, T data) {
       Node<T> aux = node;
       if(data.compareTo(node.getData()) < 0){
            if(node.getLeft() == null) node.setLeft(new Node(data));
            else{
                node.setLeft(insertR(node.getLeft(), data));
                if (height(node.getLeft()) - height(node.getRight()) == 2){
                    if (data.compareTo(node.getLeft().getData()) < 0)
                        aux = leftRotate(node);
                    else
                        aux = doublerotatewithleft(node);
                }
            }
       }
       else{
           if (node.getRight() == null) node.setRight( new Node<T>(data));
           else{
               node.setRight(insertR(node.getRight(),data));
               if( height(node.getRight()) - height(node.getLeft()) == 2){
                   if (data.compareTo(node.getRight().getData()) > 0) aux = rightRotate(node);
                   else aux = doublerotatewithright(node);

               }
           }
       }
        if ((node.getLeft() == null) && (node.getRight() != null))
            node.setHeight(node.getRight().height() + 1);
        else if ((node.getRight() == null) && (node.getLeft() != null))
            node.setHeight(node.getLeft().height() + 1);
        else
            node.setHeight(Math.max(height(node.getLeft()), height(node.getRight())) + 1);

        return aux;
    }

    public Node<T> doublerotatewithleft(Node<T> c) {
        Node<T> tmp;
        c.left = rightRotate(c.left);
        tmp = leftRotate(c);
        return tmp;
    }

    public Node<T> doublerotatewithright(Node c) {
        Node tmp;
        c.right = leftRotate(c.right);
        tmp = rightRotate(c);
        return tmp;
    }

    private Node<T> leftRotate(Node<T> node) {
        Node<T> aux = node.getLeft();
        node.setLeft(aux.getRight());
        aux.setRight(node);

        node.setHeight( Math.max(height(node.getLeft()), height(node.getRight())) +1);
        aux.setHeight( Math.max(height(aux.getLeft()), height(aux.getRight())) +1);

        return aux;
    }

    private Node<T> rightRotate(Node<T> node) {
        Node<T> aux = node.getRight();
        node.setRight(aux.getLeft());
        aux.setLeft(node);

        node.height = Math.max(height(node.getLeft()), height(node.getRight()) +1);
        aux.height = Math.max(height(aux.getLeft()), height(aux.getRight()) +1);

        return aux;

    }
    
    int getBalance(Node<T> N) {
        if (N == null)
            return 0;

        return height(N.getLeft()) - height(N.getRight());
    }

    int height(Node<T> N) {
        if (N == null)
            return -1;

        return N.height;
    }


    private class Node<T>{
        private T data;
        private int height;
        private Node<T> right = null;
        private Node<T> left = null;

        public Node(T data){
            this.data = data;
            this.height = 0;
        }

        public int height(){
            if (this != null)
                return height;
            return 0;
        }

        public T getData(){
            return this.data;
        }

        public void setData(T data){
            this.data = data;
        }

        public Node<T> getRight(){
            return this.right;
        }

        public Node<T> getLeft(){
            return this.left;
        }

        public void setRight(Node<T> node){
            this.right = node;
        }

        public void setLeft(Node<T> node){
            this.left = node;
        }

        public void setHeight(int height){
            this.height = height;
        }

    }
}