package frontend;

import backend.Client;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

public class entrypageUI extends JDialog {
    private JPanel contentPane;
    private JButton logInButton;
    private JButton signUpButton;
    Client client;

    public entrypageUI(Client client) {
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
                System.exit(0);            }
        });
        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // navigate to the Signup page
            }
        });
}


    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 9991, 1);

        entrypageUI dialog = new entrypageUI(client);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
