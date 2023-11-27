package backend.repositories;

public interface PostRepository {
    boolean createPost(long userId, String comment);

    boolean editPost(long yapId, String comment);

    boolean removePost(long yapId);

    boolean likePost(long userId, long yapId);

    boolean unlikePost(long userId, long yapId);
}
