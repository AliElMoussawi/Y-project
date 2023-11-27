package backend.repositories;

public interface PostRepository {
    boolean createPost(long userId, String comment);
}
