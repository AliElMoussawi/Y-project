package backend.services;

import backend.models.database.User;
import backend.repositories.FollowRepository;
import backend.repositories.FollowRepositoryImpl;
import backend.repositories.UserRepository;
import backend.repositories.UserRepositoryImpl;

import java.util.List;

public class FollowServiceImpl implements FollowService {

    static FollowRepository followRepository = new FollowRepositoryImpl();
    private final UserRepository userRepository = new UserRepositoryImpl();

    public boolean checkFollow(long user, long user_to_follow) {
        return followRepository.follow(user, user_to_follow);
    }

    public boolean db_Follow(long userId, long userToFollowId) throws Exception {
        return followRepository.createFollower(userId, userToFollowId);
    }

    public static boolean deleteRelationship(long user, long user_to_unfollow) {
        return followRepository.removeFollower(user, user_to_unfollow);
    }

    public User getUserById(long user) {
        return userRepository.findById(user);
    }

    public boolean createFollow(long user, long user_to_follow) throws Exception {
        if (checkFollow(user, user_to_follow))
            throw new Exception("You already follow this user");
        if (!db_Follow(user, user_to_follow))
            throw new Exception("An error occurred when inserting into Y.Follower");

        return true;
    }

    @Override
    public List<String> getFollowing(long user) throws Exception {
        try {
            return followRepository.getFollowing(user);
        } catch (Exception e) {
            throw new Exception("Error while retrieving following users", e);
        }
    }

    public boolean removeFollow(long user, long user_to_unfollow) throws Exception {
        return deleteRelationship(user, user_to_unfollow);
    }
}
