package frontend;

import backend.Client;
import backend.dto.AuthenticationDTO;
import backend.models.protocol.RequestObject;
import backend.models.protocol.ResponseObject;
import backend.utils.enums.Action;
import backend.utils.enums.Method;
import backend.utils.enums.StatusCode;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static backend.Client.createRequest;
import static frontend.LoginUI.openHomePage;

public class SignUpUI extends JDialog {
    private JPanel contentPane;
    private JButton signUpButton;
    private JTextField emailInput;
    private JTextField usernameInput;
    private JPasswordField passwordInput;
    Client client;

    public SignUpUI(Client client) {
        this.client = client;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(signUpButton);
        signUpButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            onSignUp();
        }
        });
    }

    private void onSignUp() {
        String username = usernameInput.getText().trim();
        String email = emailInput.getText().trim();
        String password = passwordInput.getText().trim();

        AuthenticationDTO authenticationDTO = new AuthenticationDTO(username, email, password);
        RequestObject requestObject = new RequestObject();
        requestObject.setAction(Action.SIGNUP);
        requestObject.setObject(authenticationDTO);
        requestObject.setMethod(Method.POST);
        requestObject.setId("signup1");
        ResponseObject responseObject = createRequest(client, requestObject);
        if(responseObject.getStatusCode() == StatusCode.OK){
            String token = String.valueOf(responseObject.getObject());
            openHomePage(client, username, token);
        }
        usernameInput.setText("");
        emailInput.setText("");
        passwordInput.setText("");
    }

    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 9991, 1);
        SignUpUI dialog = new SignUpUI(client);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
