package backend.controllers;

import backend.dto.*;
import backend.interfaces.Controller;
import backend.models.database.Post;
import backend.models.protocol.RequestObject;
import backend.models.protocol.ResponseObject;
import backend.services.*;
import backend.utils.enums.Action;
import backend.utils.enums.StatusCode;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Repeatable;
import java.util.List;

public class EventController implements Controller {
    FollowService followService = new FollowServiceImpl();
    PostService postService = new PostServiceImpl();

    CommentService commentService=new CommentServiceImpl();

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
                return removeFollow(followerId, usertoFollowID);
            }
            else if (request.getAction() == Action.GET_FOLLOWING) {
                return getFollowing(followerId);
            }
            else if (request.getAction() == Action.GET_FOLLOWERS) {
                return getFollowers(followerId);
            }
            else if (request.getAction() == Action.GET_POST){
                return getPost((EventDTO) data);
            }
        }
        else if (data instanceof PostDTO)
        {
            if(request.getAction() == Action.UPLOAD_POST) {
                PostDTO postDTO = (PostDTO) data;
                return uploadPost(postDTO);
            }
        }
        else if (data instanceof EditPostDTO)
        {
            if(request.getAction() == Action.EDIT_POST) {
                EditPostDTO postDTO = (EditPostDTO) data;
                return editPost(postDTO);
            }
            else if (request.getAction() == Action.DELETE_POST){
                EditPostDTO postDTO = (EditPostDTO) data;
                return deletePost(postDTO);
            }
        }
        else if (data instanceof LikeDTO)
        {
            if (request.getAction() == Action.LIKE_POST){
                LikeDTO postDTO = (LikeDTO) data;
                return likePost(postDTO);
            }
            else if (request.getAction() == Action.UNLIKE_POST){
                LikeDTO postDTO = (LikeDTO) data;
                return unlikePost(postDTO);
            }
        }
        else if (data instanceof CommentDTO){
            CommentDTO commentObject=(CommentDTO) data;
            String content=commentObject.getContent();
            long postId=commentObject.getPostId();
            long userId=commentObject.getUserId();
            if(request.getAction().equals(Action.COMMENT)){
                return createComment(content, postId, userId);
            }
            else if(request.getAction().equals(Action.UNCOMMENT)){
                return doUncomment(content, postId, userId);
            }

        }



        else {
                return new ResponseObject(StatusCode.BAD_REQUEST, null, "Invalid data for creating a post");
            }

        return new ResponseObject(StatusCode.BAD_REQUEST, null, "An error occurred with EventController");
    }

    @NotNull
    private ResponseObject doUncomment(String content, long postId, long userId) {
        try{
            if(commentService.unComment(postId, userId, content)){
                return new ResponseObject(StatusCode.OK, null, "Comment Removed Successfully");
            }
            else {
                return new ResponseObject(StatusCode.BAD_REQUEST, null, "Comment Removing Failed");
            }
        }
        catch (Exception e){
            return new ResponseObject(StatusCode.BAD_REQUEST, null, e.getMessage());
        }
    }

    @NotNull
    private ResponseObject createComment(String content, long postId, long userId) {
        try{
            if(commentService.comment(postId, userId, content)){
                return new ResponseObject(StatusCode.OK, null, "Comment Posted Successfully");
            }
            else {
                return new ResponseObject(StatusCode.BAD_REQUEST, null, "Comment Posting Failed");
            }
        }
        catch (Exception e){
            return new ResponseObject(StatusCode.BAD_REQUEST, null, e.getMessage());
        }
    }

    @NotNull
    private ResponseObject unlikePost(LikeDTO postDTO) {
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

    @NotNull
    private ResponseObject likePost(LikeDTO postDTO) {
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

    @NotNull
    private ResponseObject deletePost(EditPostDTO postDTO) {
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

    @NotNull
    private ResponseObject editPost(EditPostDTO postDTO) {
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

    @NotNull
    private ResponseObject uploadPost(PostDTO postDTO) {
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

    @NotNull
    private ResponseObject getPost(EventDTO data) {
        try {
            EventDTO eventDTO = data;
            List<Post> posts = postService.getPosts(eventDTO.getFollowerId());
            return new ResponseObject(StatusCode.OK, posts, "Posts retrieved successfully");
        } catch (Exception e) {
            return new ResponseObject(StatusCode.INTERNAL_SERVER_ERROR, null, "An error occurred while retrieving posts");
        }
    }

    @NotNull
    private ResponseObject getFollowers(long followerId) {
        try {
            List<String> followingUsernames = followService.getFollowers(followerId);
            return new ResponseObject(StatusCode.OK, followingUsernames, "Following users retrieved successfully");
        } catch (Exception e) {
            return new ResponseObject(StatusCode.INTERNAL_SERVER_ERROR, null, "An error occurred while retrieving following users");
        }
    }

    @NotNull
    private ResponseObject getFollowing(long followerId) {
        try {
            List<String> followingUsernames = followService.getFollowing(followerId);
            return new ResponseObject(StatusCode.OK, followingUsernames, "Following users retrieved successfully");
        } catch (Exception e) {
            return new ResponseObject(StatusCode.INTERNAL_SERVER_ERROR, null, "An error occurred while retrieving following users");
        }
    }

    @NotNull
    private ResponseObject removeFollow(long followerId, Long usertoFollowID) {
        try {
            if(!followService.checkFollow(followerId, usertoFollowID)){
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
