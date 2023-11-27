package backend.services;

import java.util.List;

public interface FollowService {
    boolean createFollow(long userId, long userToFollowId) throws Exception;
    boolean checkFollow(long userId,long userToFollowId) throws Exception;
    boolean removeFollow(long user, long user_to_unfollow) throws Exception;

    List<String> getFollowing(long user) throws Exception;


}
