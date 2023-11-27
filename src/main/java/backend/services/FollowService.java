package backend.services;

public interface FollowService {
    boolean createFollow(long userId, long userToFollowId) throws Exception;
    boolean checkFollow(long userId,long userToFollowId) throws Exception;
    boolean removeFollow(long user, long user_to_unfollow) throws Exception;

}
