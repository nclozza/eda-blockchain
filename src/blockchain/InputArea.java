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
        String output = textSelector(text);
        textArea.append(output + newline);
        textField.selectAll();

        //Make sure the new text is visible, even if there
        //was a selection in the text area.
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    public String textSelector(String text){
        while (true) {
            String input = text;

            if (input.matches("^(add\\s\\d*)$")) {
                Integer aux = Integer.parseInt(input.substring(4));
                System.out.println("Voy a agregar este nodo: " + aux);
                return "Agregaste un elemento";
            }
            else if (input.matches("^(lookup\\s\\d*)$")) {
                Integer aux = Integer.parseInt(input.substring(7));
                System.out.println("Voy a buscar este nodo: " + aux);
                return "Buscaste un elemento";
            }
            else if (input.matches("^(remove\\s\\d*)$")) {
                Integer aux = Integer.parseInt(input.substring(7));
                System.out.println("Borraste este nodo: " + aux);
                return "Borraste un elemento";

            } else if (input.matches("^(zeros\\s\\d*)$")) {
                Integer aux = Integer.parseInt(input.substring(6));
                System.out.println("Seteaste esta cantidad de zeros: " + aux);
                return "Seteaste los ceros";

            } else if (input.matches("^(validate)$")) {
                return "Quisiste validar";

            } else if (input.matches("^(modify)$")){
                return "Quisiste modificar el archivo, todavia no esta listo";

            } else if (input.matches("^(exit)$")){
                return "Nos vemos";

            } else {
                return "Horrible input";
            }
        }
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

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                InputArea.createAndShowGUI();
            }
        });
    }

}

