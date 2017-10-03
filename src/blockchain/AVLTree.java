package blockchain;

public class AVLTree<T>{

    private Node<T> header;

    public AVLTree(T data){
        header = new Node<T>(data, header);
    }

    public AVLTree(){
        header = null;
    }

    public boolean addNode(T data){
        if (data != null) {
            Node<T> aux = header;
            Node<T> aux2 = null;
            while (aux != null){
                aux2 = aux;
                aux = aux.getRight();
            }
            aux2.setRight(new Node<T>(data, aux2));
            return true;
        }
        return false;
    }

    //Just to test
    public boolean addLeft(T data){
        if (data != null) {
            Node<T> aux = header;
            Node<T> aux2 = null;
            while (aux != null){
                aux2 = aux;
                aux = aux.getLeft();
            }
            aux2.setLeft(new Node<T>(data, aux2));
            return true;
        }
        return false;
    }

    //Just to test
    public void printAVL(){
        Node<T> aux = header;
        int maxLevel = 5;
        String auxS = " ";

        System.out.println(aux + aux.getData().toString());
        for (int i = 0 ; i < maxLevel && aux.getRight() != null ; i++){
            System.out.println(auxS + "\\");
            aux = aux.getRight();
            auxS += " ";
            System.out.println(auxS + aux.getData().toString());
            auxS += " ";
        }
    }

    public int getHeight(){
        return header.getHeight();
    }

    public int getMaxLevel(){
        return header.getMaxLevel();
    }

    public boolean isBalanced(){
        int val = this.getHeight();
        return val == 1 || val == -1 || val == 0;
    }

    private class Node<T>{
        private T data;
        private Node<T> right;
        private Node<T> left;
        private Node<T> previous;

        public Node(T data, Node<T> previous){
            this.data = data;
            this.previous = previous;
        }

        //Works fine
        public int getMaxLevel(){
            return Math.max(getLevelR(right), getLevelL(left));
        }

        private int getLevelR(Node<T> right){
            if (right != null)
                return 1 + Math.max(getLevelR(right.getRight()), getLevelL(right.getLeft()));
            return 0;
        }

        private int getLevelL(Node<T> left){
            if (left != null)
                return 1 + Math.max(getLevelR(left.getRight()), getLevelL(left.getLeft()));
            return 0;
        }

        public T getData(){
            return this.data;
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

        //RightHeight - LeftHeight = 1 ó = 0 ó = -1
        //Works fine
        public int getHeight(){
            if (this != null) return getHeightR(this.right)- getHeightR(this.left);
            else {return 0;}
        }

        private int getHeightR(Node<T> node){
           if (node == null) return 0;

           int leftHeight = getHeightR(node.left);
           int rightHeight = getHeightR(node.right);

           if (leftHeight > rightHeight) return leftHeight+1;
           else return rightHeight+1;
        }
    }

}
