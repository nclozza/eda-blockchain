package visual;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import blockchain.Node;

/**
 * The BinaryTreeView class is an implementation of a graphical interface to display the AVL tree.
 */
public class BinaryTreeView<T> extends JPanel {
    /**
     * The tree to be displayed.
     */
    protected Node<T> tree;

    /**
     * The maximum height of any tree drawn so far. This is used to avoid the tree jumping around when nodes are removed
     * from the tree.
     */
    protected int maxHeight;

    /**
     * The font used for the tree nodes.
     */
    protected Font font = new Font("Courier New", 1, 30);


    public BinaryTreeView(Node<T> tree, int width, int height) throws IOException {
        Color background = new Color(240, 122, 0);
        setBackground(background);
        setForeground(Color.black);

        /**
         * Create the window to display the tree. Closing the window will terminate the program.
         */
        JFrame f = new JFrame("BinaryTree View");
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        f.getContentPane().add(this, BorderLayout.CENTER);
        f.setSize(new Dimension(width, height));
        f.setVisible(true);
        
        setTree(tree);
    }

    /*
    * Set the display to show the given tree.
    */
    public void setTree(Node<T> t) {
        if (t == null) {
            maxHeight = 0;
        } else {
            tree = t;
            maxHeight = tree.getHeight();
        }
    }

    /*
    * Invoke this method whenever you would like the window
    * to be refreshed, such as after updating the tree in some
    * way.
    */
    public void refresh(Node<T> node) {
        this.tree = node;
        paintImmediately(0, 0, getWidth(), getHeight());
    }

    /*
    * Draw the contents of the tree into the given Graphics
    * context.
    * It will place all info between minX and maxX in the x-direction,
    * starting at location y in the y-direction.  Levels of the tree
    * will be separated by yStep pixels.
    */
    protected void drawTree(Graphics g, int minX, int maxX, int y, int yStep, Node<T> tree) {
        if (tree != null) {
            String s = tree.getData().toString();
            g.setFont(font);
            FontMetrics fm = g.getFontMetrics();

            int width = fm.stringWidth(s);
            int height = fm.getHeight();
            int xSep = Math.min((maxX - minX) / 8, 10);

            g.drawString(s, (minX + maxX) / 2 - width / 2, y + yStep / 2);

            if (!(tree.getLeft() == null)) {
                // if left tree not empty, draw line to it and recursively
                // draw that tree
                g.drawLine((minX + maxX) / 2 - xSep, y + yStep / 2 + 5, (minX + (minX + maxX) / 2) / 2,
                y + yStep + yStep / 2 - height);
                drawTree(g, minX, (minX + maxX) / 2, y + yStep, yStep, tree.getLeft());
            }

            if (!(tree.getRight() == null)) {
                // same thing for right subtree.
                g.drawLine((minX + maxX) / 2 + xSep, y + yStep / 2 + 5, (maxX + (minX + maxX) / 2) / 2,
                y + yStep + yStep / 2 - height);
                drawTree(g, (minX + maxX) / 2, maxX, y + yStep, yStep, tree.getRight());
            }
        }
    }


    /*
    * paint method unherited from the Swing library.  Just
    * calls drawTree whenever the window needs to be repainted.
    */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);      //clears the background
        if (tree != null) {
          int width = getWidth();
          int height = getHeight();
          maxHeight = Math.max(tree.getHeight(), maxHeight);
          int treeHeight = maxHeight;
          drawTree(g, 0, width, 0, height / (treeHeight + 1), tree);
        }
    }

    public void closeWindow(){
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }
}