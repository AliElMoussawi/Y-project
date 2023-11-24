package backend.controllers;

import backend.Server;
import backend.interfaces.Controller;
import backend.models.AuthenticationDTO;
import backend.models.FollowClientDTO;
import backend.models.RequestObject;
import backend.models.ResponseObject;
import backend.security.UserCredentialsManager;
import backend.utils.enums.Action;
import backend.utils.enums.StatusCode;

import java.util.Map;

public class FollowUserController implements Controller {

    UserCredentialsManager userCredentialsManager = UserCredentialsManager.getInstance();
    @Override
    public ResponseObject handleRequest(RequestObject request) {
        if (request.getAction() == Action.FOLLOW) {
            Object data = request.getObject();
            if (data instanceof FollowClientDTO) {

                FollowClientDTO credentials = (FollowClientDTO) data;
                AuthenticationDTO user=credentials.getUser();
                AuthenticationDTO user_to_follow=credentials.getUser_to_follow();

                if (!exists(user_to_follow)) {
                    return new ResponseObject(StatusCode.NOT_FOUND, null, "Client does not exist");
                }
                else if(follows(user,user_to_follow)){
                    return new ResponseObject(StatusCode.BAD_REQUEST, null, "You already follow this user");
                }
                else {
                    updateFollows(user,user_to_follow);
                    return new ResponseObject(StatusCode.OK, null, "Followed User " + user_to_follow.getUsername());
                }
            }
        }
        return new ResponseObject(StatusCode.BAD_REQUEST, null, "An error occurred with Following the user");

    }
    public void updateFollows(AuthenticationDTO user, AuthenticationDTO user_to_follow){
        user.getFollowing().add(user_to_follow);
        user_to_follow.getFollowers().add(user);
    }
    private boolean exists(AuthenticationDTO client){
        return userCredentialsManager.isUser(client.getUsername(), client.getPassword());
    }

    private boolean follows(AuthenticationDTO user,AuthenticationDTO user_to_follow){
        return user.getFollowing().contains(user_to_follow);
    }


}