package backend.controllers;

import backend.interfaces.Controller;
import backend.models.AuthenticationDTO;
import backend.models.RequestObject;
import backend.models.ResponseObject;
import backend.security.Token;
import backend.security.UserCredentialsManager;
import backend.utils.enums.Action;
import backend.utils.enums.StatusCode;
import lombok.Getter;

@Getter
public class AuthenticationController implements Controller {
    UserCredentialsManager userCredentialsManager = UserCredentialsManager.getInstance();

    @Override
    public ResponseObject handleRequest(RequestObject request) {
        switch (request.getAction()) {
            case LOGIN:
            case SIGNUP:
                Object data = request.getObject();
                if (data instanceof AuthenticationDTO) {
                    AuthenticationDTO cred = (AuthenticationDTO) data;
                    AuthenticationDTO credentials =new AuthenticationDTO(cred.getUsername(),cred.getPassword());
                    return processAuthentication(credentials, request.getAction());
                } else {
                    return new ResponseObject(StatusCode.BAD_REQUEST, null,"Invalid authentication data");
                }
            default:
                return new ResponseObject(StatusCode.BAD_REQUEST, null,"Invalid action for AuthenticationController");
        }
    }

    private ResponseObject processAuthentication(AuthenticationDTO authData, Action action) {
        if (action == Action.LOGIN) {
            return authenticateUser(authData.getUsername(), authData.getPassword());
        } else if (action == Action.SIGNUP) {
            return registerUser(authData.getUsername(), authData.getPassword());
        } else {
            return new ResponseObject(StatusCode.BAD_REQUEST, null,"Invalid action for authentication");
        }
    }
    public ResponseObject authenticateUser(String username, String password) {
        try {
            String token = userCredentialsManager.authenticate(username, password);
            return new ResponseObject(StatusCode.OK, token, "Authentication successful");

        } catch (Exception e) {
            return new ResponseObject(StatusCode.UNAUTHORIZED, null, "Authentication failed");
        }
    }
    public ResponseObject registerUser(String username, String password) {
        try {
            String token = userCredentialsManager.signUp(username, password);
            return new ResponseObject(StatusCode.OK, token, "Signed up successful");

        } catch (Exception e) {
            return new ResponseObject(StatusCode.UNAUTHORIZED, null, e.getMessage());
        }
    }


}
