package backend.services;

import backend.models.database.Post;
import backend.repositories.PostRepository;
import backend.repositories.PostRepositoryImpl;

import java.util.List;

import static backend.Server.broadcastMessage;

public class PostServiceImpl implements PostService{
    private PostRepository postRepository = new PostRepositoryImpl();

    @Override
    public boolean createPost(long userId, String comment) throws Exception {
        return postRepository.createPost(userId, comment);
    }

    @Override
    public boolean editPost(long yapId, String comment) throws Exception {
        return postRepository.editPost(yapId, comment);
    }

    @Override
    public boolean removePost(long yapId) throws Exception
    {
        return postRepository.removePost(yapId);
    }

    @Override
    public boolean likePost(long userId,long yapId) throws Exception
    {
        return postRepository.likePost(userId,yapId);
    }

    @Override
    public boolean unlikePost(long userId,long yapId) throws Exception
    {
        return postRepository.unlikePost(userId,yapId);
    }

    @Override
    public List<Post> getPosts(long userId) throws Exception
    {
        return postRepository.getPosts(userId);
    }
}
