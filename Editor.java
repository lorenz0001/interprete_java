import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Editor {
    public static class Gestisci implements ActionListener {
        JTextArea input;
        JTextArea output;

        Gestisci(JTextArea input, JTextArea output) {
            this.input = input;
            this.output = output;
        }

        public void actionPerformed(ActionEvent e) {
            output.setText("");
            Compilatore.compila(input.getText(), output, "");
        }
    }
    public static void main() {
        JFrame frame = new JFrame("compilatore");
        
        JTextArea areaCodice = new JTextArea(15, 50);
        JScrollPane scrollCodice = new JScrollPane(areaCodice);

        JTextArea areaOutput = new JTextArea(5, 50);
        areaOutput.setEditable(false);
        JScrollPane scrollOutput = new JScrollPane(areaOutput);

        JMenuBar menuBar = new JMenuBar();
        JMenu menuCompila = new JMenu("Compila");
        JMenuItem voce = new JMenuItem("Esegui compilazione");
        
        voce.addActionListener(new Gestisci(areaCodice, areaOutput));

        menuCompila.add(voce);
        menuBar.add(menuCompila);
        frame.setJMenuBar(menuBar);

        frame.setLayout(new BorderLayout());
        frame.add(scrollCodice, BorderLayout.CENTER);
        frame.add(scrollOutput, BorderLayout.SOUTH);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
} 
