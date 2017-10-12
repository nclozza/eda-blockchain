package blockchain;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by cderienzo on 10/12/2017.
 */
class AVLTreeTest {

    AVLTree<Integer> setup(){
        AVLTree<Integer> avlTree = new AVLTree<>();

        try{
            avlTree.insert(2);
            avlTree.insert(3);
            avlTree.insert(6);
            avlTree.insert(10);
            avlTree.insert(1);
            avlTree.insert(8);
            avlTree.insert(70);
        }catch(Exception e){

        }
        return avlTree;
    }

    @org.junit.jupiter.api.Test
    void getRoot() {
        AVLTree tree = setup();
        assertTrue(tree.getRoot().getData().equals(3));
    }

    @org.junit.jupiter.api.Test
    void height() {
        AVLTree tree = setup();
        assertTrue(tree.height(tree.getRoot()) == 3);

    }

    @org.junit.jupiter.api.Test
    void contains() {
        AVLTree tree = setup();
        assertTrue(tree.contains(2) && !tree.contains(69));
    }

    @org.junit.jupiter.api.Test
    void insert() {
        AVLTree tree = setup();
        boolean thrown = false;
        assertFalse(tree.contains(69));
        try {
            tree.insert(69);
        } catch(DuplicateNodeInsertException e){
            thrown = true;
        }
        assertTrue(tree.contains(69) && !thrown);
        try {
            tree.insert(69);
        } catch(DuplicateNodeInsertException e){
            thrown = true;
        }
        assertTrue(thrown);
    }

    @org.junit.jupiter.api.Test
    void delete() {
        AVLTree tree = setup();
        boolean thrown = false;
        assertTrue(tree.contains(70));

        try {
            tree.delete(70);
        } catch(NodeNotFoundException e){
            thrown = true;
        }

        assertTrue(!tree.contains(70) && !thrown);

        try {
            tree.delete(69);
        } catch(NodeNotFoundException e){
            thrown = true;
        }
        assertTrue(thrown);
    }

    @org.junit.jupiter.api.Test
    void getModifiedNodesList() {

    }

    @org.junit.jupiter.api.Test
    void byLevel() {

    }

    @org.junit.jupiter.api.Test
    void preOrder() {

    }

    @org.junit.jupiter.api.Test
    void inOrder() {

    }

}