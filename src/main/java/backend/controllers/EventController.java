package backend.controllers;

import backend.dto.EditPostDTO;
import backend.dto.EventDTO;
import backend.dto.LikeDTO;
import backend.dto.PostDTO;
import backend.interfaces.Controller;
import backend.models.protocol.RequestObject;
import backend.models.protocol.ResponseObject;
import backend.services.FollowService;
import backend.services.FollowServiceImpl;
import backend.services.PostService;
import backend.services.PostServiceImpl;
import backend.utils.enums.Action;
import backend.utils.enums.StatusCode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EventController implements Controller {
    FollowService followService = new FollowServiceImpl();
    PostService postService = new PostServiceImpl();

    @Override
    public ResponseObject handleRequest(RequestObject request) {
        Object data = request.getObject();

        if (data instanceof EventDTO) {
            EventDTO credentials = (EventDTO) data;
            long followerId=credentials.getFollowerId();
            Long usertoFollowID = credentials.getToFollowId();
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
            else if (request.getAction() == Action.GET_FOLLOWING) {
                try {
                    List<String> followingUsernames = followService.getFollowing(followerId);
                    return new ResponseObject(StatusCode.OK, followingUsernames, "Following users retrieved successfully");
                } catch (Exception e) {
                    return new ResponseObject(StatusCode.INTERNAL_SERVER_ERROR, null, "An error occurred while retrieving following users");
                }
            }
            else if (request.getAction() == Action.GET_FOLLOWERS) {
                try {
                    List<String> followingUsernames = followService.getFollowers(followerId);
                    return new ResponseObject(StatusCode.OK, followingUsernames, "Following users retrieved successfully");
                } catch (Exception e) {
                    return new ResponseObject(StatusCode.INTERNAL_SERVER_ERROR, null, "An error occurred while retrieving following users");
                }
            }
        }
        else if (data instanceof PostDTO)
        {
            if(request.getAction() == Action.UPLOAD_POST) {
                PostDTO postDTO = (PostDTO) data;
                try {
                    if (postService.createPost(postDTO.getUserId(), postDTO.getContent())) {
                        return new ResponseObject(StatusCode.OK, null, "Post created successfully");
                    } else {
                        return new ResponseObject(StatusCode.BAD_REQUEST, null, "Failed to create post");
                    }
                } catch (Exception e) {
                    return new ResponseObject(StatusCode.INTERNAL_SERVER_ERROR, null, "An error occurred while creating the post");
                }
            }
        }
        else if (data instanceof EditPostDTO)
        {
            if(request.getAction() == Action.EDIT_POST) {
                EditPostDTO postDTO = (EditPostDTO) data;
                try {
                    if (postService.editPost(postDTO.getYapId(), postDTO.getContent())) {
                        return new ResponseObject(StatusCode.OK, null, "Post edited successfully");
                    } else {
                        return new ResponseObject(StatusCode.BAD_REQUEST, null, "Failed to edit post");
                    }
                } catch (Exception e) {
                    return new ResponseObject(StatusCode.INTERNAL_SERVER_ERROR, null, "An error occurred while creating the post");
                }
            }
            else if (request.getAction() == Action.DELETE_POST){
                EditPostDTO postDTO = (EditPostDTO) data;
                try {
                    if (postService.removePost(postDTO.getYapId())) {
                        return new ResponseObject(StatusCode.OK, null, "Post deleted successfully");
                    } else {
                        return new ResponseObject(StatusCode.BAD_REQUEST, null, "Failed to delete post");
                    }
                } catch (Exception e) {
                    return new ResponseObject(StatusCode.INTERNAL_SERVER_ERROR, null, "An error occurred while creating the post");
                }
            }
        }
        else if (data instanceof LikeDTO)
        {
            if (request.getAction() == Action.LIKE_POST){
                LikeDTO postDTO = (LikeDTO) data;
                try {
                    if (postService.likePost(postDTO.getUserId(), postDTO.getYapId())) {
                        return new ResponseObject(StatusCode.OK, null, "Post liked successfully");
                    } else {
                        return new ResponseObject(StatusCode.BAD_REQUEST, null, "Failed to like post");
                    }
                } catch (Exception e) {
                    return new ResponseObject(StatusCode.INTERNAL_SERVER_ERROR, null, "An error occurred while creating the post");
                }
            }
            else if (request.getAction() == Action.UNLIKE_POST){
                LikeDTO postDTO = (LikeDTO) data;
                try {
                    if (postService.unlikePost(postDTO.getUserId(), postDTO.getYapId())) {
                        return new ResponseObject(StatusCode.OK, null, "Post unliked successfully");
                    } else {
                        return new ResponseObject(StatusCode.BAD_REQUEST, null, "Failed to unlike post");
                    }
                } catch (Exception e) {
                    return new ResponseObject(StatusCode.INTERNAL_SERVER_ERROR, null, "An error occurred while creating the post");
                }
            }
        }
        else {
                return new ResponseObject(StatusCode.BAD_REQUEST, null, "Invalid data for creating a post");
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
