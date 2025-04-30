import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

public class CreaFinestra{
    public static class Gestisci implements ActionListener{
        JTextField mail;
        JTextField password;
        JFrame frame;
        Gestisci(JTextField mail, JTextField password, JFrame frame){
            this.mail = mail;
            this.password = password;
            this.frame = frame;
        }
        public void actionPerformed(ActionEvent e){
            String m = mail.getText();
            String p = password.getText();
            if(e.getActionCommand().equals("Recupera")){
                try{
                    p = Funzioni.recupera("credenziali.txt",m);
                if(p == null){
                    return;
                } else {
                    password.setText(p);
                }
                }catch(IOException ex){
                
                }
            } else {
                App.app(m,p);
                frame.dispose();
            }
        }
    }
    public static void crea(String messaggio) {
        JFrame frame = new JFrame("Avviso");
        JPanel panel = new JPanel(new GridBagLayout());
        frame.setResizable(false);
        frame.setSize(250, 70);
        frame.setLocationRelativeTo(null);
        
        JLabel label = new JLabel(messaggio);
        panel.add(label);
        
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void recuperaPassword(String mail, Frame f) {
        f.dispose();
        
        JFrame frame = new JFrame("Recupera Password");
        frame.setResizable(false);
        frame.setSize(350, 300);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font f1 = new Font("Arial", Font.BOLD, 20);
        Font f2 = new Font("Arial", Font.BOLD, 15);

        JLabel titolo = new JLabel("Recupera password", SwingConstants.CENTER);
        titolo.setFont(f1);
        JLabel mailLabel = new JLabel("Mail");
        JLabel passwordLabel = new JLabel("Password");
        mailLabel.setFont(f2);
        passwordLabel.setFont(f2);

        JTextField mailField = new JTextField(10);
        mailField.setText(mail);
        JTextField passwordField = new JTextField(10);
        passwordField.setEditable(false);
        
        Gestisci gestore = new Gestisci(mailField, passwordField, frame); 
        
        JButton recuperaButton = new JButton("Recupera");
        recuperaButton.addActionListener(gestore);
        JButton tornaIndietro = new JButton("Torna al login");
        tornaIndietro.addActionListener(gestore);

        frame.add(titolo, gbc);
        frame.add(mailLabel, gbc);
        frame.add(mailField, gbc);
        frame.add(passwordLabel, gbc);
        frame.add(passwordField, gbc);
        frame.add(recuperaButton, gbc);
        frame.add(tornaIndietro, gbc);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    public static void compilatore() {       
        JFrame frame = new JFrame("Compilatore wow7");
        frame.setResizable(false);
        frame.setSize(350, 300);
        
        String taskBar[] = {"File","Modifica","Visualizza","Compila","Aiuto"};
        
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        JToolBar toolBar = new JToolBar();
        for(int i=0;i<taskBar.length;i++)
            toolBar.add(new JButton(taskBar[i]));
        frame.add(toolBar, "North");

        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}