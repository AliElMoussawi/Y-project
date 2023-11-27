package frontend;

import backend.Client;
import backend.dto.AuthenticationDTO;
import backend.models.protocol.RequestObject;
import backend.models.protocol.ResponseObject;
import backend.utils.enums.Action;
import backend.utils.enums.Method;
import backend.utils.enums.StatusCode;

import javax.swing.*;
import java.awt.event.*;

import static backend.Client.createRequest;

public class LoginUI extends JDialog {
    private JPanel contentPane;
    private JFormattedTextField usernameOrEmail;
    private JPasswordField passwordInput;
    private JButton loginButton;
    Client client; // Reference to ChatClient

    public LoginUI(Client client) {
        this.client = client;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(loginButton);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onLogin();
            }
        });
    }

    private void onLogin() {
        String username = usernameOrEmail.getText().trim();
        String password = passwordInput.getText().trim();
        AuthenticationDTO authenticationDTO = new AuthenticationDTO(username, null, password);
        RequestObject requestObject = new RequestObject();
        requestObject.setAction(Action.LOGIN);
        requestObject.setObject(authenticationDTO);
        requestObject.setMethod(Method.POST);
        ResponseObject responseObject = createRequest(client, requestObject);
        if(responseObject.getStatusCode() == StatusCode.OK){
            String token = String.valueOf(responseObject.getObject());
            // navigate to the second page we can pass the token as argument
            openHomePage(client, token);
        }
        usernameOrEmail.setText("");
        passwordInput.setText("");
    }
    public static void openHomePage(Client client, String token){
        SwingUtilities.invokeLater(() -> {
            HomePage twitterHomePage = new HomePage(client, token);
            twitterHomePage.setVisible(true);
        });
    }
    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 9991, 1);
        LoginUI dialog = new LoginUI(client);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
