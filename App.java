import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class App {
    public static class Gestisci implements ActionListener {
        JTextField mail;
        JPasswordField password;
        JFrame frame;

        Gestisci(JTextField mail, JPasswordField password, JFrame frame) {
            this.mail = mail;
            this.password = password;
            this.frame = frame;
        }

        public void actionPerformed(ActionEvent e) {
            String m = mail.getText();
            String p = password.getText();
            String c = e.getActionCommand();
            try {
                if (c.equals("Accedi")) {
                    Funzioni.login("credenziali.txt", m, p, frame);
                } else if (c.equals("Recupera password")) {
                    CreaFinestra.recuperaPassword(m, frame);
                } else if (c.equals("Registrati")) {
                    Funzioni.registra("credenziali.txt", m, p, frame);
                }
            } catch (IOException ec) {
                
            }
        }
    }

    public static void main(String args[]){
        app("","");
    }
    
    public static void app(String mail, String password) {
        JFrame frame = new JFrame("APP");
        
        frame.setSize(500, 400);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel principale = new JPanel();
        principale.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titolo = new JLabel("Login", SwingConstants.CENTER);
        JLabel mailLabel = new JLabel("Mail");
        JLabel passwordLabel = new JLabel("Password");

        Font f1 = new Font("Arial", Font.BOLD, 30);
        Font f2 = new Font("Arial", Font.BOLD, 20);

        titolo.setFont(f1);
        mailLabel.setFont(f2);
        passwordLabel.setFont(f2);

        JTextField mailField = new JTextField(10);
        JPasswordField passwordField = new JPasswordField(10);

        if(mail != null){
            mailField.setText(mail);
            mailField.revalidate();
            mailField.repaint();
        }
        if(password != null){
            passwordField.setText(password);
            passwordField.revalidate();
            passwordField.repaint();
        }
        
        JButton accediButton = new JButton("Accedi");
        JButton registraButton = new JButton("Registrati");
        JButton recuperaPass = new JButton("Recupera password");

        Gestisci g = new Gestisci(mailField, passwordField, frame);

        accediButton.addActionListener(g);
        registraButton.addActionListener(g);
        recuperaPass.addActionListener(g);

        principale.add(titolo, gbc);
        principale.add(mailLabel, gbc);
        principale.add(mailField, gbc);
        principale.add(passwordLabel, gbc);
        principale.add(passwordField, gbc);
        principale.add(accediButton, gbc);
        principale.add(registraButton, gbc);
        principale.add(recuperaPass, gbc);

        frame.add(principale);
        
        frame.setVisible(true);
    }
}
