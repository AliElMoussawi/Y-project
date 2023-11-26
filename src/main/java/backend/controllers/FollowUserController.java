package backend.controllers;

import backend.DatabaseConnection;
import backend.Server;
import backend.interfaces.Controller;
import backend.models.AuthenticationDTO;
import backend.models.FollowClientDTO;
import backend.models.RequestObject;
import backend.models.ResponseObject;
import backend.security.UserCredentialsManager;
import backend.utils.enums.Action;
import backend.utils.enums.StatusCode;

import java.sql.Connection;
import java.util.Map;

public class FollowUserController implements Controller {

    UserCredentialsManager userCredentialsManager = UserCredentialsManager.getInstance();
    DatabaseConnection conn=new DatabaseConnection();
    @Override
    public ResponseObject handleRequest(RequestObject request) {
        if (request.getAction() == Action.FOLLOW) {
            Object data = request.getObject();
            if (data instanceof FollowClientDTO) {

                FollowClientDTO credentials = (FollowClientDTO) data;
                AuthenticationDTO user=credentials.getUser();
                String user_to_follow=credentials.getUser_to_follow();

                if (!exists(user_to_follow)) {
                    return new ResponseObject(StatusCode.NOT_FOUND, null, "Client does not exist");
                }
                else if(follows(user,user_to_follow)){
                    return new ResponseObject(StatusCode.BAD_REQUEST, null, "You already follow this user");
                }
                else {
                    updateFollows(user,user_to_follow);
                    return new ResponseObject(StatusCode.OK, null, "Followed User " + user_to_follow);
                }
            }
        }
        return new ResponseObject(StatusCode.BAD_REQUEST, null, "An error occurred with Following the user");

    }
    public void updateFollows(AuthenticationDTO user, String user_to_follow){
        conn.db_Follow(user.getUsername(),user_to_follow);
    }
    private boolean exists(String client){
        return conn.db_exists(client);
    }

    private boolean follows(AuthenticationDTO user,String user_to_follow){
        return conn.db_follows(user.getUsername(),user_to_follow);
    }


}