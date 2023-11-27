package backend.repositories;

public interface PostRepository {
    boolean createPost(long userId, String comment);

    boolean editPost(long yapId, String comment);
}
