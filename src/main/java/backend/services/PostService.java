package backend.services;

public interface PostService {
    boolean createPost(long userId, String comment) throws Exception;

    boolean editPost(long yapId, String comment) throws Exception;

    boolean removePost(long yapId) throws Exception;
}
