package backend.services;

import backend.models.database.User;

public interface FollowService {
    public boolean createRelationship(String user, String user_to_follow) throws Exception;
    public boolean removerRelationship(String user, String user_to_unfollow) throws Exception;

    User getUserByUsernameOrEmail(String user_to_follow);
}
