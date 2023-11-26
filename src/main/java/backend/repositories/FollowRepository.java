package backend.repositories;

public interface FollowRepository {
    boolean follows(long user, long user_to_follow);

    boolean createFollower(long user, long user_to_follow);

    public boolean removeFollower(long user, long user_to_follow);
}
