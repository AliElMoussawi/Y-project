package frontend;

import backend.Client;
import backend.models.AuthenticationDTO;
import backend.models.RequestObject;
import backend.utils.enums.Action;
import backend.utils.enums.Method;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

public class ChatClientUI extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JFormattedTextField usernameInput;
    private JTextField passwordInput;
    Client client; // Reference to ChatClient

    public ChatClientUI(Client client) {
        this.client = client;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onOK();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() throws IOException {
        // Get message from formattedTextField1 and send it
        String username = usernameInput.getText().trim();
        String password = passwordInput.getText().trim();
        AuthenticationDTO authenticationDTO = new AuthenticationDTO(username, password);
        RequestObject requestObject = new RequestObject();
        requestObject.setAction(Action.SIGNUP);
        requestObject.setObject(authenticationDTO);
        requestObject.setMethod(Method.POST);
        client.sendObject(requestObject);
        usernameInput.setText("");
        passwordInput.setText("");
    }
    public void setClient(Client client) {
        this.client = client;
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 9991); // Replace with actual server address and port
        ChatClientUI dialog = new ChatClientUI(client);
        dialog.pack();
        dialog.setVisible(true);
    }
}
