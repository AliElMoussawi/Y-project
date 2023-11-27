package backend.controllers;

import backend.interfaces.Controller;
import backend.dto.AuthenticationDTO;
import backend.models.database.User;
import backend.models.protocol.RequestObject;
import backend.models.protocol.ResponseObject;
import backend.services.UserServiceImpl;
import backend.utils.enums.Action;
import backend.utils.enums.StatusCode;


public class AuthenticationController implements Controller {
    UserServiceImpl userServiceImpl = new UserServiceImpl();
    @Override
    public ResponseObject handleRequest(RequestObject request) {
        switch (request.getAction()) {
            case LOGIN:
            case SIGNUP:
                Object data = request.getObject();
                if (data instanceof AuthenticationDTO) {
                    AuthenticationDTO cridentials = (AuthenticationDTO) data;
                    return processAuthentication(cridentials, request.getAction());
                } else {
                    return new ResponseObject(StatusCode.BAD_REQUEST, null,"Invalid authentication data");
                }
            default:
                return new ResponseObject(StatusCode.BAD_REQUEST, null,"Invalid action for AuthenticationController");
        }
    }

    private ResponseObject processAuthentication(AuthenticationDTO authData, Action action) {
        if (action == Action.LOGIN) {
            String userNameOrEmail = (authData.getUsername() == null) ? authData.getEmail() : authData.getUsername();
            return authenticateUser(userNameOrEmail, authData.getPassword());
        } else if (action == Action.SIGNUP) {
            return registerUser(authData.getUsername(), authData.getEmail(), authData.getPassword());
        } else {
            return new ResponseObject(StatusCode.BAD_REQUEST, null,"Invalid action for authentication");
        }
    }

    public ResponseObject authenticateUser(String username, String password) {
        try {
            String token = userServiceImpl.authenticate(username, password);
            return new ResponseObject(StatusCode.OK, token, "Authentication successful");

        } catch (Exception e) {
            return new ResponseObject(StatusCode.UNAUTHORIZED, null, e.getMessage());
        }
    }

    public ResponseObject registerUser(String username, String email, String password) {
        try {
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            String token = userServiceImpl.createUser(user);
            return new ResponseObject(StatusCode.OK, token, "Signed up successful");

        } catch (Exception e) {
            return new ResponseObject(StatusCode.UNAUTHORIZED, null, e.getMessage());
        }
    }

}