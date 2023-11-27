package backend.repositories;

import java.util.List;

public interface FollowRepository {
    boolean follow(long userId, long UserToFollowId);

    boolean createFollower(long user, long user_to_follow);

    boolean removeFollower(long user, long user_to_follow);

    List<String> getFollowing(long user);

    List<String> getFollowers(long user);

}
