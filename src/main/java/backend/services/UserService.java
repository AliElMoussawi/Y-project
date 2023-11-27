package backend.services;

import backend.models.database.User;

public interface UserService {
    User getUserByUsernameOrEmail(String usernameOrEmail);

    String createUser(User user) throws Exception;

    String authenticate(String usernameOrEmail, String password) throws Exception;
}
