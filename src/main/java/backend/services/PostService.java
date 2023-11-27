package backend.services;

import backend.models.database.Post;

import java.util.List;

public interface PostService {
    boolean createPost(long userId, String comment) throws Exception;

    boolean editPost(long yapId, String comment) throws Exception;

    boolean removePost(long yapId) throws Exception;

    boolean likePost(long userId, long yapId) throws Exception;

    boolean unlikePost(long userId, long yapId) throws Exception;

    List<Post> getPosts(long userid) throws Exception;
}
