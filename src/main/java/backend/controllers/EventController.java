package backend.controllers;

import backend.dto.EventDTO;
import backend.interfaces.Controller;
import backend.models.protocol.RequestObject;
import backend.models.protocol.ResponseObject;
import backend.services.FollowService;
import backend.services.FollowServiceImpl;
import backend.services.UserServiceImpl;
import backend.services.UserServiceImpl;
import backend.utils.enums.Action;
import backend.utils.enums.StatusCode;

public class EventController implements Controller {
    FollowService followService=new FollowServiceImpl();

    @Override
    public ResponseObject handleRequest(RequestObject request) throws Exception {
        Object data = request.getObject();
        if (request.getAction() == Action.FOLLOW) {
            if (data instanceof EventDTO) {

                EventDTO credentials = (EventDTO) data;
                String user=credentials.getFollowerUsername();
                String user_to_follow=credentials.getFollowedUser();

                if (followService.getUserByUsernameOrEmail(user_to_follow)==null) {
                    return new ResponseObject(StatusCode.NOT_FOUND, null, "An error occurred with retrieving the user_to_follow");
                }
                else if(followService.createRelationship(user,user_to_follow)){
                    return new ResponseObject(StatusCode.OK, null, "Followed User " + user_to_follow);

                }
                else {
                    return new ResponseObject(StatusCode.BAD_REQUEST, null, "An error occurred with Follow");
                }
            }
        }
        else if(request.getAction()==Action.UNFOLLOW){
            if (data instanceof EventDTO){
                EventDTO credentials = (EventDTO) data;
                String user=credentials.getFollowerUsername();
                String user_to_follow=credentials.getFollowedUser();

                if (followService.getUserByUsernameOrEmail(user_to_follow)==null) {
                    return new ResponseObject(StatusCode.NOT_FOUND, null, "An error occurred with retrieving the user_to_follow");
                }
                else if(followService.removerRelationship(user,user_to_follow)){
                    return new ResponseObject(StatusCode.OK, null, "UnFollowed User " + user_to_follow);
                }
                else {
                    return new ResponseObject(StatusCode.BAD_REQUEST, null, "An error occurred with Unfollow");
                }
            }
        }
        return new ResponseObject(StatusCode.BAD_REQUEST, null, "An error occurred with EventController");

    }
}
