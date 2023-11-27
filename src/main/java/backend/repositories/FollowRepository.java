package backend.repositories;

public interface FollowRepository {
    boolean follow(long userId, long UserToFollowId);

    boolean createFollower(long user, long user_to_follow);

    boolean removeFollower(long user, long user_to_follow);
}
