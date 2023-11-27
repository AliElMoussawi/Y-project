package backend.repositories;

import backend.dto.PostDTO;

import java.util.List;

public interface PostRepository {

    PostDTO getPostById(int id);

    List<PostDTO> getPostByUserId(int userId);

    List<PostDTO> getPostsForFollowers(int userId);
    boolean insertPost(PostDTO post);
    boolean updatePost(PostDTO post);
    boolean deletePost(PostDTO post);

}
