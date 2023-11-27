package backend.services;

import backend.repositories.PostRepository;
import backend.repositories.PostRepositoryImpl;

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
}
