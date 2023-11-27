package backend.repositories;

public interface CommentRepository {
    boolean createComment(long postId, long userId, String content);
    boolean removeComment(long postId, long  userId, String content);
}
