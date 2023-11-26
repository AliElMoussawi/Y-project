package backend.services;

import backend.models.database.User;
import backend.repositories.FollowRepository;
import backend.repositories.FollowRepositoryImpl;
import backend.repositories.UserRepository;
import backend.repositories.UserRepositoryImpl;

public class FollowServiceImpl implements FollowService{

    FollowRepository followRepository=new FollowRepositoryImpl();
    UserRepository userRepository=new UserRepositoryImpl();
    public boolean checkFollow(long user, long user_to_follow){
        return followRepository.follows(user,user_to_follow);
    }

    public boolean updateRelationship(long user, long user_to_follow){
        return followRepository.createFollower(user,user_to_follow);
    }

    public boolean deleteRelationship(long user, long user_to_unfollow){
        return followRepository.removeFollower(user,user_to_unfollow);
    }
    public User getUserByUsernameOrEmail(String user) {
        return userRepository.findByUsernameOrEmail(user);
    }
    public boolean createRelationship(String user, String user_to_follow) throws Exception {
        User user1=getUserByUsernameOrEmail(user);
        User user2=getUserByUsernameOrEmail(user_to_follow);
        if(user2==null)
            throw new Exception("User specified does not exist");
        long u_1= user1.getId();
        long u_2=user2.getId();

        if(checkFollow(u_1,u_2))
            throw new Exception("You already follow this user");
        if(!updateRelationship(u_1,u_2))
            throw new Exception("An error occurred when inserting into Y.Follower");

        return true;
    }



    public boolean removerRelationship(String user, String user_to_unfollow) throws Exception {
        User user1=getUserByUsernameOrEmail(user);
        User user2=getUserByUsernameOrEmail(user_to_unfollow);
        if(user2==null)
            throw new Exception("User specified does not exist");
        long u_1= user1.getId();
        long u_2=user2.getId();

        if(!checkFollow(u_1,u_2))
            throw new Exception("There is no relationship");

        return deleteRelationship(u_1,u_2);
    }
}
