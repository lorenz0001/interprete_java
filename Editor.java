import javax.swing.*;
import javax.swing.event.*;
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

    public static class AggiornaNumeriRiga implements DocumentListener {
        JTextArea codice;
        JTextArea numeri;

        AggiornaNumeriRiga(JTextArea codice, JTextArea numeri) {
            this.codice = codice;
            this.numeri = numeri;
        }

        private void aggiorna() {
            int righe = codice.getLineCount();
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i <= righe; i++) {
                sb.append(i).append("\n");
            }
            numeri.setText(sb.toString());
        }

        public void insertUpdate(DocumentEvent e) {
            aggiorna();
        }

        public void removeUpdate(DocumentEvent e) {
            aggiorna();
        }

        public void changedUpdate(DocumentEvent e) {
            aggiorna();
        }
    }

    public static void main() {
        JFrame frame = new JFrame("Compilatore");

        // Area codice e numeri di riga
        JTextArea areaCodice = new JTextArea(15, 50);
        JTextArea numeriRiga = new JTextArea("1\n");
        numeriRiga.setEditable(false);
        numeriRiga.setBackground(Color.LIGHT_GRAY);

        areaCodice.getDocument().addDocumentListener(new AggiornaNumeriRiga(areaCodice, numeriRiga));

        JScrollPane scrollCodice = new JScrollPane(areaCodice);
        scrollCodice.setRowHeaderView(numeriRiga);

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
        voce.addActionListener(new Gestisci(areaCodice, areaErr, areaOut));
        menuCompila.add(voce);
        menuBar.add(menuCompila);

        // Font selector (senza generics)
        Integer[] dimensioniFont = {10, 12, 14, 16, 18, 20, 24, 28, 32};
        JComboBox selettoreFont = new JComboBox(dimensioniFont);
        selettoreFont.setSelectedItem(32);
        selettoreFont.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int dimensione = (int) selettoreFont.getSelectedItem();
                Font nuovoFont = new Font("Monospaced", Font.PLAIN, dimensione);
                areaCodice.setFont(nuovoFont);
                areaErr.setFont(nuovoFont);
                areaOut.setFont(nuovoFont);
                numeriRiga.setFont(nuovoFont);
            }
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
        numeriRiga.setFont(defaultFont);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
