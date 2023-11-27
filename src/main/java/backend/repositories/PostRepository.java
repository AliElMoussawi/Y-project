package backend.repositories;

import backend.models.database.Post;

import java.util.List;

public interface PostRepository {
    boolean createPost(long userId, String comment);

    boolean editPost(long yapId, String comment);

    boolean removePost(long yapId);

    boolean likePost(long userId, long yapId);

    boolean unlikePost(long userId, long yapId);

    List<Post> getPosts(long userId);

}
