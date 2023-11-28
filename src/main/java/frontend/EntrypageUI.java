package frontend;

import backend.Client;

import javax.swing.*;
import java.awt.event.*;

public class EntrypageUI extends JDialog {
    private JPanel contentPane;
    private JButton logInButton;
    private JButton signUpButton;
    Client client;

    public EntrypageUI(Client client) {
        this.client = client;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(logInButton);
        getRootPane().setDefaultButton(signUpButton);

        logInButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LoginUI dialog = new LoginUI(client);
                dialog.pack();
                dialog.setVisible(true);
                EntrypageUI.this.setVisible(false); // Show login form
            }
        });

        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SignUpUI dialog = new SignUpUI(client);
                dialog.pack();
                dialog.setVisible(true);
                System.exit(0);                 }
        });
    }

    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 9991, 1);

        EntrypageUI dialog = new EntrypageUI(client);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
