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

        // Menu
        JMenuBar menuBar = new JMenuBar();

        // Menu Compila
        JMenu menuCompila = new JMenu("Compila");
        JMenuItem voce = new JMenuItem("Esegui compilazione");
        voce.addActionListener(new Gestisci(areaCodice, areaOutput));
        menuCompila.add(voce);
        menuBar.add(menuCompila);

        // ComboBox per selezione grandezza font
        Integer[] dimensioniFont = {10, 12, 14, 16, 18, 20, 24, 28, 32};
        JComboBox selettoreFont = new JComboBox(dimensioniFont);
        selettoreFont.setSelectedItem(14);  // Default
        selettoreFont.addActionListener(e -> {
            int dimensione = (int)selettoreFont.getSelectedItem();
            Font nuovoFont = new Font("Arial", Font.PLAIN, dimensione);
            areaCodice.setFont(nuovoFont);
            areaOutput.setFont(nuovoFont);
        });

        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(new JLabel(" Font: "));
        menuBar.add(selettoreFont);

        // Frame layout
        frame.setJMenuBar(menuBar);
        frame.setLayout(new BorderLayout());
        frame.add(scrollCodice, BorderLayout.CENTER);
        frame.add(scrollOutput, BorderLayout.SOUTH);

        // Imposta font iniziale
        Font defaultFont = new Font(Font.MONOSPACED, Font.PLAIN, 14);
        areaCodice.setFont(defaultFont);
        areaOutput.setFont(defaultFont);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
