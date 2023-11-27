package backend.services;

public interface CommentService {
    public boolean comment(long postId, long userId, String content);
    public boolean unComment(long postId, long userId, String content);
}
