package backend.controllers;

import backend.dto.EventDTO;
import backend.interfaces.Controller;
import backend.models.protocol.RequestObject;
import backend.models.protocol.ResponseObject;
import backend.services.FollowService;
import backend.services.FollowServiceImpl;
import backend.utils.enums.Action;
import backend.utils.enums.StatusCode;
import org.jetbrains.annotations.NotNull;

public class EventController implements Controller {
    FollowService followService = new FollowServiceImpl();

    @Override
    public ResponseObject handleRequest(RequestObject request) {
        Object data = request.getObject();
        if (data instanceof EventDTO) {
            EventDTO credentials = (EventDTO) data;
            long followerId=credentials.getFollowerId();
            long usertoFollowID = credentials.getToFollowId();
            if (request.getAction() == Action.FOLLOW) {
                return createFollow(usertoFollowID, followerId);
            } else if (request.getAction() == Action.UNFOLLOW) {
                try {
                    if(!followService.checkFollow(followerId,usertoFollowID)){
                        return new ResponseObject(StatusCode.OK, null, "You have no relationship with the user");
                    }
                    else if (followService.removeFollow(followerId, usertoFollowID)) {
                        return new ResponseObject(StatusCode.OK, null, "UnFollowed User " + usertoFollowID);
                    } else {
                        return new ResponseObject(StatusCode.BAD_REQUEST, null, "An error occurred with unfollow");
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return new ResponseObject(StatusCode.BAD_REQUEST, null, "An error occurred with EventController");
    }

    @NotNull
    private ResponseObject createFollow(long usertoFollowID, long follower_id) {
        try {
            if (followService.createFollow(follower_id, usertoFollowID)) {
                return new ResponseObject(StatusCode.OK, null, "Followed User " + usertoFollowID);

            } else {
                return new ResponseObject(StatusCode.BAD_REQUEST, null, "An error occurred with Follow");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
