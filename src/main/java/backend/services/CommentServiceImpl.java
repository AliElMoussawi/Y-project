package backend.services;

import backend.repositories.CommentRepository;
import backend.repositories.CommentRepositoryImpl;

public class CommentServiceImpl implements CommentService{
    CommentRepository repository=new CommentRepositoryImpl();
    @Override
    public boolean comment(long postId, long userId, String content) {
        return repository.createComment(postId,userId,content);
    }

    @Override
    public boolean unComment(long postId, long userId, String content) {
        return repository.removeComment(postId,userId,content);
    }
}
