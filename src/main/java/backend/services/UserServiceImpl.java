package backend.services;

import backend.models.database.User;
import backend.repositories.UserRepository;
import backend.repositories.UserRepositoryImpl;
import backend.security.Token;

import java.sql.SQLException;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository = new UserRepositoryImpl();
    private static final Token token = Token.getInstance();

    public UserServiceImpl() {
    }

    public User getUserById(int id) {
        return userRepository.findById(id);
    }

    public User getUserByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsernameOrEmail(usernameOrEmail);
    }


    public String authenticate(String usernameOrEmail, String password) throws Exception {
        User user = getUserByUsernameOrEmail(usernameOrEmail);
        if (user == null) {
            throw new Exception("User doesn't exist");
        }
        if (!user.getPassword().equals(password)) {
            throw new Exception("Wrong password");
        }
        return token.createToken(usernameOrEmail);
    }

    public String createUser(User user) throws Exception {
        if(getUserByUsernameOrEmail(user.getUsername()) != null) {
            throw new Exception("Username already exist");
        }
        if(getUserByUsernameOrEmail(user.getEmail()) != null) {
            throw new Exception("Email already exist");
        }
        if(userRepository.save(user)) {
            return token.createToken(user.getUsername());
        }
        throw new Exception("Error while creating the account");
    }


}