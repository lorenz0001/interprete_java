import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Editor {
    public static class Gestisci implements ActionListener {
        JTextArea input;
        JTextArea err;
        JTextArea out;

        Gestisci(JTextArea input, JTextArea err, JTextArea out) {
            this.input = input;
            this.err = err;
            this.out = out;
        }

        public void actionPerformed(ActionEvent e) {
            err.setText("");
            out.setText("");
            Compilatore.compila(input.getText(), err, out, "");
        }
    }

    public static void main() {
        JFrame frame = new JFrame("Compilatore");

        // Area codice
        JTextArea areaCodice = new JTextArea(15, 50);
        JScrollPane scrollCodice = new JScrollPane(areaCodice);

        // Area errori
        JTextArea areaErr = new JTextArea(5, 25);
        areaErr.setEditable(false);
        JScrollPane scrollErr = new JScrollPane(areaErr);

        // Area output
        JTextArea areaOut = new JTextArea(5, 25);
        areaOut.setEditable(false);
        JScrollPane scrollOut = new JScrollPane(areaOut);

        // Menu
        JMenuBar menuBar = new JMenuBar();

        JMenu menuCompila = new JMenu("Compila");
        JMenuItem voce = new JMenuItem("Esegui compilazione");
        voce.addActionListener(new Gestisci(areaCodice, areaErr, areaOut)); // <-- compila va in areaErr
        menuCompila.add(voce);
        menuBar.add(menuCompila);

        // Font selector
        Integer[] dimensioniFont = {10, 12, 14, 16, 18, 20, 24, 28, 32};
        JComboBox<Integer> selettoreFont = new JComboBox<>(dimensioniFont);
        selettoreFont.setSelectedItem(32);
        selettoreFont.addActionListener(e -> {
            int dimensione = (int) selettoreFont.getSelectedItem();
            Font nuovoFont = new Font("Monospaced", Font.PLAIN, dimensione);
            areaCodice.setFont(nuovoFont);
            areaErr.setFont(nuovoFont);
            areaOut.setFont(nuovoFont);
        });

        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(new JLabel(" Font: "));
        menuBar.add(selettoreFont);

        // Area inferiore con errori e output affiancati
        JPanel areaBassa = new JPanel(new GridLayout(1, 2));
        areaBassa.add(scrollErr);
        areaBassa.add(scrollOut);

        // Setup frame
        frame.setJMenuBar(menuBar);
        frame.setLayout(new BorderLayout());
        frame.add(scrollCodice, BorderLayout.CENTER);
        frame.add(areaBassa, BorderLayout.SOUTH);

        // Font iniziale
        Font defaultFont = new Font("Monospaced", Font.PLAIN, 32);
        areaCodice.setFont(defaultFont);
        areaErr.setFont(defaultFont);
        areaOut.setFont(defaultFont);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
