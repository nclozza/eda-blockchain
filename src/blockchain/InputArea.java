package blockchain;

/**
 * Created by cderienzo on 10/8/2017.
 */
/**
 * Created by cderienzo on 10/8/2017.
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class InputArea extends JPanel implements ActionListener {
    protected JTextField textField;
    protected JTextArea textArea;
    private final static String newline = "\n";

    public InputArea() {
        super(new GridBagLayout());

        textField = new JTextField(20);
        textField.addActionListener(this);
        textArea = new JTextArea(10, 30);
        textArea.setEditable(false);
        textField.setFont(new Font("Arial",Font.BOLD,20));
        textArea.setFont(new Font("Arial",Font.BOLD,20));
        textField.setBackground(Color.black);
        textField.setForeground(Color.green);
        textArea.setBackground(Color.black);
        textArea.setForeground(Color.green);

        JScrollPane scrollPane = new JScrollPane(textArea);

        //Add Components to this panel.
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;

        c.fill = GridBagConstraints.HORIZONTAL;
        add(textField, c);

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 100.0;
        c.weighty = 100.0;
        add(scrollPane, c);
    }


    public void actionPerformed(ActionEvent evt) {
        String text = textField.getText();
        //String output = textSelector(text);
        textArea.append(text + newline);
        textField.selectAll();

        //Make sure the new text is visible, even if there
        //was a selection in the text area.
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    public static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Terminal Emulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add contents to the window.
        frame.add(new InputArea());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public String getInputString() {
      return textField.getText();
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                InputArea.createAndShowGUI();
            }
        });
    }

    public void closeWindow(){
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

}

