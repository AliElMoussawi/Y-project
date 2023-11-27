package backend.services;

public interface PostService {
    boolean createPost(long userId, String comment) throws Exception;
}
